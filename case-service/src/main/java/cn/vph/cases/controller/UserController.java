package cn.vph.cases.controller;

import cn.vph.cases.entity.User;
import cn.vph.cases.service.UserService;
import cn.vph.common.Result;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-17 18:10
 **/
@RestController
@RequestMapping("users")
@Api(value = "UserController", tags = {"用户服务接口"})
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 登录接口
     */
    @PostMapping("login")
    public Result<User> login(@RequestBody User user) {
        return Result.success(userService.login(user.getNickname(), user.getPassword()));
    }

}
