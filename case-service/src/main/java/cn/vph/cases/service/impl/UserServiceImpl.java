package cn.vph.cases.service.impl;

import cn.hutool.crypto.SecureUtil;
import cn.vph.cases.controller.request.RegisterRequest;
import cn.vph.cases.entity.User;
import cn.vph.cases.mapper.UserMapper;
import cn.vph.cases.service.UserService;
import cn.vph.cases.util.CaptchaUtil;
import cn.vph.cases.util.SessionUtil;
import cn.vph.common.CommonErrorCode;
import cn.vph.common.util.AssertUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-17 18:23
 **/
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
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
    public static String convert(String password){
        return SecureUtil.md5(password).substring(6,24);
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
        simpleEmailMessage.setText("您的验证码是："+captcha);
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

}
