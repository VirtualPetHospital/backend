package cn.vph.cases.service.impl;

import cn.hutool.crypto.SecureUtil;
import cn.vph.cases.controller.request.RegisterRequest;
import cn.vph.cases.entity.Disease;
import cn.vph.cases.entity.Medcase;
import cn.vph.cases.entity.User;
import cn.vph.cases.entity.UserMedcase;
import cn.vph.cases.mapper.DiseaseMapper;
import cn.vph.cases.mapper.MedcaseMapper;
import cn.vph.cases.mapper.UserMapper;
import cn.vph.cases.mapper.UserMedcaseMapper;
import cn.vph.cases.service.UserService;
import cn.vph.cases.util.CaptchaUtil;
import cn.vph.cases.util.SessionUtil;
import cn.vph.common.CommonConstant;
import cn.vph.common.CommonErrorCode;
import cn.vph.common.util.AssertUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-17 18:23
 **/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DiseaseMapper diseaseMapper;
    @Autowired
    private MedcaseMapper medcaseMapper;
    @Autowired
    private UserMedcaseMapper userMedcaseMapper;
    @Autowired
    private SessionUtil sessionUtil;
    @Autowired
    private CaptchaUtil captchaUtil;
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    /**
     * 转md5
     */
    public static String convert(String password) {
        return SecureUtil.md5(password).substring(6, 24);
    }

    @Override
    public User login(String nickname, String password) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getNickname, nickname);
        User user = userMapper.selectOne(wrapper);
        AssertUtil.isNotNull(user, CommonErrorCode.USER_NOT_EXIST);
        AssertUtil.isEqual(user.getPassword(), convert(password), CommonErrorCode.WRONG_PASSWORD_OR_NICKNAME);
        sessionUtil.setSession(user);
        return user;
    }

    @Override
    public User me() {
        Integer userId = sessionUtil.getUserId();
        return userMapper.selectById(userId);
    }

    @Override
    @Transactional
    public User register(RegisterRequest registerRequest) {
        // 从redis中获取验证码
        String captcha = captchaUtil.getCaptcha(registerRequest.getEmail());
        // 判断验证码是否正确
        AssertUtil.isEqual(captcha, registerRequest.getCaptcha(), CommonErrorCode.WRONG_CAPTCHA);
        User user = new User(registerRequest);
        user.setPassword(convert(user.getPassword()));
        userMapper.insert(user);
        return user;
    }

    @Override
    @Transactional
    public Object delete(Integer userId) {
        // 先查是否存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserId, userId);
        User user = userMapper.selectOne(wrapper);
        AssertUtil.isNotNull(user, CommonErrorCode.USER_NOT_EXIST);
        // 删除用户
        userMapper.deleteById(userId);
        return null;
    }

    @Override
    @Transactional
    public User update(User user) {
        // 先查是否存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getNickname, user.getNickname());
        User user1 = userMapper.selectOne(wrapper);
        AssertUtil.isNotNull(user1, CommonErrorCode.USER_NOT_EXIST);
        // 是否是当前用户
        AssertUtil.isEqual(sessionUtil.getUserId(), user1.getUserId(), CommonErrorCode.NOT_CURRENT_USER);
        // 不能改Type
        AssertUtil.isEqual(user1.getType(), user.getType(), CommonErrorCode.ILLEGAL_USER_INFO);
        // 更新用户
        user.setUserId(sessionUtil.getUserId());
        userMapper.updateById(user);
        return user;
    }

    @Override
    public Object sendCaptcha(String email) {
        // 发送验证码
        SimpleMailMessage simpleEmailMessage = new SimpleMailMessage();
        simpleEmailMessage.setFrom(from);
        simpleEmailMessage.setTo(email);
        simpleEmailMessage.setSubject("虚拟宠物医院系统注册验证码");
        String captcha = captchaUtil.setCaptcha(email);
        simpleEmailMessage.setText("您的验证码是：" + captcha);
        mailSender.send(simpleEmailMessage);
        return null;
    }

    @Override
    public Object logout() {
        sessionUtil.invalidate();
        return null;
    }

    @Override
    public Boolean checkNickname(String nickname) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getNickname, nickname);
        User user = userMapper.selectOne(wrapper);
        return user == null;
    }

    @Override
    public Boolean checkEmail(String email) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, email);
        User user = userMapper.selectOne(wrapper);
        return user == null;
    }

    @Override
    public IPage<?> list(Integer pageNum, Integer pageSize, String type, String nicknameKeyword, Integer sortByNickname) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (type != null && !type.isEmpty()) {
            wrapper.eq(User::getType, type);
        }
        if (nicknameKeyword != null && !nicknameKeyword.isEmpty()) {
            wrapper.like(User::getNickname, nicknameKeyword);
        }
        if (sortByNickname != null) {
            switch (sortByNickname) {
                case CommonConstant.SORT_ASC:
                    wrapper.orderByAsc(User::getNickname);
                    break;
                case CommonConstant.SORT_DESC:
                    wrapper.orderByDesc(User::getNickname);
                    break;
                default:
                    break;
            }
        }
        return userMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public IPage<?> medcases(Integer pageNum, Integer pageSize, String medcaseInfoKeyword, String medcaseNameKeyword, String disease, Integer sortByViewTime) {
        MPJLambdaWrapper<Medcase> wrapper = new MPJLambdaWrapper<>();
        // 病例描述模糊查询
        if (medcaseInfoKeyword != null && !medcaseInfoKeyword.isEmpty()) {
            wrapper.like(Medcase::getInfoDescription, medcaseInfoKeyword);
        }
        // 病例名模糊查询
        if (medcaseNameKeyword != null && !medcaseNameKeyword.isEmpty()) {
            wrapper.like(Medcase::getName, medcaseNameKeyword);
        }
        // 疾病名模糊查询
        if (disease != null && !disease.isEmpty()) {
            LambdaQueryWrapper<Disease> diseaseWrapper = new LambdaQueryWrapper<>();
            diseaseWrapper.eq(Disease::getName, disease);
            Disease disease1 = diseaseMapper.selectOne(diseaseWrapper);
            AssertUtil.isNotNull(disease1, CommonErrorCode.DISEASE_NOT_EXIST);
            wrapper.like(Medcase::getDiseaseId, disease1.getDiseaseId());
        }
        wrapper.selectAll(Medcase.class)
                .leftJoin(UserMedcase.class, UserMedcase::getMedcaseId, Medcase::getMedcaseId)
                .eq(UserMedcase::getUserId, sessionUtil.getUserId());

        // 排序
        if (sortByViewTime != null) {
            switch (sortByViewTime) {
                case CommonConstant.SORT_ASC:
                    wrapper.orderByAsc(UserMedcase::getViewTime);
                    break;
                case CommonConstant.SORT_DESC:
                    wrapper.orderByDesc(UserMedcase::getViewTime);
                    break;
                default:
                    break;
            }
        }

        return medcaseMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }

}
