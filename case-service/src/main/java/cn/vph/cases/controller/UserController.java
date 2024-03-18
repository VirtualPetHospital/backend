package cn.vph.cases.controller;

import cn.vph.cases.controller.request.RegisterRequest;
import cn.vph.cases.entity.User;
import cn.vph.cases.service.UserService;
import cn.vph.common.Result;
import cn.vph.common.annotation.Administrator;
import cn.vph.common.annotation.Student;
import com.alibaba.fastjson.JSONObject;
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
     * @param registerRequest
     * @return
     */
    @PostMapping
    public Result<User> register(@RequestBody RegisterRequest registerRequest) {
        return Result.success(userService.register(registerRequest));
    }



    /**
     * 删除用户接口
     */
    @Administrator
    @DeleteMapping("{userId}")
    public Result<Object> delete(@PathVariable Integer userId) {
        return Result.success(userService.delete(userId));
    }

    /**
     * 更新用户信息
     */
    @PutMapping
    public Result<User> update(@RequestBody User user) {
        //TODO 更新用户信息
        return Result.success(userService.update(user));
    }

    /**
     * 登录接口
     */
    @PostMapping("login")
    public Result<User> login(@RequestBody User user) {
        return Result.success(userService.login(user.getNickname(), user.getPassword()));
    }
    /**
     * 用户登出
     */
    @PostMapping("logout")
    public Result<Object> logout() {
        //TODO 登出逻辑
        return Result.success(null);
    }
    /**
     * 检查用户名是否可用
     */
    @GetMapping("nickname")
    public Result<Boolean> checkNickname(@RequestParam String nickname) {
        //TODO 检查用户名是否可用
        return Result.success(true);
    }
    /**
     * 检查邮箱是否可用
     */
    @GetMapping("email")
    public Result<Boolean> checkEmail(@RequestParam String email) {
        //TODO 检查邮箱是否可用
        return Result.success(true);
    }
    /**
     * 查询用户列表
     */
    @GetMapping
    public Result<Object> list() {
        //TODO 查询用户列表
        return Result.success(null);
    }
    /**
     * 查询用户查看过的病例
     */
    @GetMapping("medcases")
    public Result<Object> medcases() {
        //TODO 查询用户查看过的病例
        return Result.success(null);
    }

    /**
     * 查询用户参与的考试
     */
    @GetMapping("exams")
    public Result<Object> exams() {
        //TODO 查询用户参与的考试
        return Result.success(null);
    }

    /**
     * 请求验证码
     */
    @PostMapping("captcha")
    public Result<Object> captcha(@RequestBody JSONObject jsonObject) {
        String email = jsonObject.getString("email");
        return Result.success(userService.sendCaptcha(email));
    }
}
