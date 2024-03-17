package cn.vph.cases.service.impl;

import cn.hutool.crypto.SecureUtil;
import cn.vph.cases.entity.User;
import cn.vph.cases.mapper.UserMapper;
import cn.vph.cases.service.UserService;
import cn.vph.cases.util.SessionUtil;
import cn.vph.common.CommonErrorCode;
import cn.vph.common.util.AssertUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
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

}
