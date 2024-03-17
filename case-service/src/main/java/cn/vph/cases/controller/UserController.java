package cn.vph.cases.controller;

import cn.vph.cases.entity.User;
import cn.vph.cases.service.UserService;
import cn.vph.common.Result;
import cn.vph.common.annotation.Administrator;
import cn.vph.common.annotation.Student;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     * 获取当前用户信息
     */
    @Student
    @GetMapping("me")
    public Result<User> me() {
        return Result.success(userService.me());
    }

    /**
     * 注册接口
     *
     * @param user
     * @return
     */
    @PostMapping
    public Result<User> register(@RequestBody User user) {
        //TODO
        return Result.success(userService.register(user));
    }

    /**
     * 登录接口
     */
    @PostMapping("login")
    public Result<User> login(@RequestBody User user) {
        return Result.success(userService.login(user.getNickname(), user.getPassword()));
    }

    /**
     * 删除用户接口
     */
    @Administrator
    @DeleteMapping("{userId}")
    public Result<Object> delete(@PathVariable Integer userId) {
        return Result.success(userService.delete(userId));
    }


}
