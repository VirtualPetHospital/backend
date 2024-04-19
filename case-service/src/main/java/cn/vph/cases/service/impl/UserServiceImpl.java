package cn.vph.cases.service.impl;

import cn.hutool.crypto.SecureUtil;
import cn.vph.cases.clients.FileFeignClient;
import cn.vph.cases.controller.request.RegisterRequest;
import cn.vph.cases.controller.response.UserResponse;
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

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    @Autowired
    private FileFeignClient fileFeignClient;

    @Value("${spring.mail.username}")
    private String from;

    /**
     * 转md5
     */
    public static String convert(String password) {
        return SecureUtil.md5(password).substring(6, 24);
    }

    @Override
    public UserResponse login(String nickname, String password) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getNickname, nickname);
        User user = userMapper.selectOne(wrapper);
        AssertUtil.isNotNull(user, CommonErrorCode.USER_NOT_EXIST);
        AssertUtil.isEqual(user.getPassword(), convert(password), CommonErrorCode.WRONG_PASSWORD_OR_NICKNAME);
        sessionUtil.setSession(user);
        return new UserResponse(user);
    }

    @Override
    public UserResponse me() {
        Integer userId = sessionUtil.getUserId();
        return new UserResponse(userMapper.selectById(userId));
    }

    @Override
    @Transactional
    public UserResponse register(RegisterRequest registerRequest) {

        // 邮箱是否合法
        AssertUtil.isTrue(isEmailValid(registerRequest.getEmail()), CommonErrorCode.ILLEGAL_EMAIL);

        // 从redis中获取验证码
        String captcha = captchaUtil.getCaptcha(registerRequest.getEmail());
        // 判断验证码是否正确
        AssertUtil.isEqual(captcha, registerRequest.getCaptcha(), CommonErrorCode.WRONG_CAPTCHA);
        // 判断邮箱是否重复
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, registerRequest.getEmail());
        User user1 = userMapper.selectOne(wrapper);
        AssertUtil.isNull(user1, CommonErrorCode.EMAIL_ALREADY_EXIST);
        // 判断用户名是否存在
        LambdaQueryWrapper<User> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(User::getNickname, registerRequest.getNickname());
        User user2 = userMapper.selectOne(wrapper1);
        AssertUtil.isNull(user2, CommonErrorCode.NICKNAME_ALREADY_EXIST);

        User user = new User(registerRequest);
        user.setPassword(convert(user.getPassword()));
        user.setUpgradeProgress(CommonConstant.EXAM_NUM_FOR_UPGRADE);
        user.setLevel(CommonConstant.MIN_LEVEL);
        userMapper.insert(user);
        return new UserResponse(user);
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
        // 删除用户头像
        fileFeignClient.delete(user.getAvatarUrl());
        return null;
    }

    @Override
    @Transactional
    public UserResponse update(User user) {
        // 根据userId 查用户是否存在
        User user1 = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUserId, sessionUtil.getUserId()));
        AssertUtil.isNotNull(user1, CommonErrorCode.USER_NOT_EXIST);
        // 查询用户名是否重复
        User nicknameUser = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getNickname, user.getNickname()));
        AssertUtil.isTrue(nicknameUser == null || nicknameUser.getUserId().equals(user1.getUserId()), CommonErrorCode.NICKNAME_ALREADY_EXIST);
        // 不能改Type
        AssertUtil.isEqual(user1.getType(), user.getType(), CommonErrorCode.ILLEGAL_USER_INFO);
        // 更新用户
        user.setUserId(sessionUtil.getUserId());
        userMapper.updateById(user);
        // 如果更新了头像，删除原头像
        if (user.getAvatarUrl() != null && !user.getAvatarUrl().equals(user1.getAvatarUrl())) {
            fileFeignClient.delete(user1.getAvatarUrl());
        }
        return new UserResponse(user);
    }

    @Override
    public Object sendCaptcha(String email) {
        // 检查邮箱是否已存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, email);
        User user = userMapper.selectOne(wrapper);
        AssertUtil.isNull(user, CommonErrorCode.EMAIL_ALREADY_EXIST);
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
        // 检查是否登录
        sessionUtil.invalidate();
        return null;
    }

    @Override
    public void checkNickname(String nickname) {
        String validStr = "^[a-zA-Z0-9_]+$";
        AssertUtil.isTrue(nickname.matches(validStr), CommonErrorCode.ILLEGAL_USER_INFO);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getNickname, nickname);
        User user = userMapper.selectOne(wrapper);
        AssertUtil.isNull(user, CommonErrorCode.NICKNAME_ALREADY_EXIST);
    }

    @Override
    public void checkEmail(String email) {

//        AssertUtil.isTrue(isEmailValid(email), CommonErrorCode.ILLEGAL_EMAIL);

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, email);
        User user = userMapper.selectOne(wrapper);
        AssertUtil.isNull(user, CommonErrorCode.EMAIL_ALREADY_EXIST);
    }

    @Override
    public IPage<?> list(Integer pageNum, Integer pageSize, String type, String nicknameKeyword, Integer sortByNickname) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(User::getUserId, User::getNickname, User::getType, User::getAvatarUrl, User::getLevel);
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

    @Override
    public void upgrade(Integer numCurrentLevel, Integer userId, String sessionId) {
        User user = userMapper.selectById(userId);
        AssertUtil.isNotNull(user, CommonErrorCode.USER_NOT_EXIST);
        if (numCurrentLevel < CommonConstant.EXAM_NUM_FOR_UPGRADE) {
            user.setUpgradeProgress(CommonConstant.EXAM_NUM_FOR_UPGRADE - numCurrentLevel);
        } else {
            // 满足升级条件
            if (user.getLevel() >= CommonConstant.MAX_LEVEL) {
                return;
            }
            user.setUpgradeProgress(CommonConstant.EXAM_NUM_FOR_UPGRADE);
            user.setLevel(user.getLevel() + 1);
            // 更新session
            sessionUtil.updateSession(user, sessionId);
        }
        userMapper.updateById(user);
    }

    @Override
    public List<String> getNicknames(List<Integer> userIds) {
        // 根据userIds列表查出每个用户的nickname
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(User::getUserId, userIds);
        // 先查出所有用户
        List<User> users = userMapper.selectList(wrapper);
        // 创建user id 到 nickname的映射
        Map<Integer, String> nicknameMap = users.stream().collect(Collectors.toMap(User::getUserId, User::getNickname));

        // 根据map找到user id 对应的Nickname
        return userIds.stream().map(nicknameMap::get).collect(Collectors.toList());
    }

    private boolean isEmailValid(String email) {
        String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher emailMatcher = pattern.matcher(email);
        return emailMatcher.matches();
    }

}
