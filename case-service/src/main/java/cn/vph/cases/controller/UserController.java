package cn.vph.cases.controller;

import cn.vph.cases.controller.request.RegisterRequest;
import cn.vph.cases.entity.User;
import cn.vph.cases.service.UserService;
import cn.vph.common.Result;
import cn.vph.common.annotation.Administrator;
import cn.vph.common.annotation.Student;
import cn.vph.common.controller.BaseController;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-17 18:10
 **/
@RestController
@RequestMapping("users")
@Api(value = "UserController", tags = {"用户服务接口"})
public class UserController extends BaseController {
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
    @Student
    @PutMapping
    public Result<User> update(@Validated @RequestBody User user) {
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
    @Student
    @PostMapping("logout")
    public Result<Object> logout() {
        return Result.success(userService.logout());
    }

    /**
     * 检查用户名是否可用
     */
    @GetMapping("nickname")
    public Result<Boolean> checkNickname(@NotNull @RequestParam String nickname) {
        return Result.success(userService.checkNickname(nickname));
    }

    /**
     * 检查邮箱是否可用
     */
    @GetMapping("email")
    public Result<Boolean> checkEmail(@RequestParam String email) {
        return Result.success(userService.checkEmail(email));
    }

    /**
     * 查询用户列表
     */
    @Administrator
    @GetMapping
    public Result list(
            @RequestParam(value = "page_num", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "page_size", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "nickname_keyword", required = false) String nicknameKeyword,
            @RequestParam(value = "sort_by_nickname", required = false) Integer sortByNickname
    ) {
        return Result.success(super.getData(userService.list(pageNum, pageSize, type, nicknameKeyword, sortByNickname)));
    }

    /**
     * 查询用户查看过的病例
     */
    @Student
    @GetMapping("medcases")
    public Result medcases(
            @RequestParam(value = "page_num", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "page_size", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "medcase_info_keyword", required = false) String medcaseInfoKeyword,
            @RequestParam(value = "medcase_name_keyword", required = false) String medcaseNameKeyword,
            @RequestParam(value = "disease", required = false) String disease,
            @RequestParam(value = "sort_by_view_time", required = false) Integer sortByViewTime
    ) {

        return Result.success(super.getData(userService.medcases(pageNum, pageSize, medcaseInfoKeyword, medcaseNameKeyword, disease, sortByViewTime)));
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
        // TODO 字段检查
        String email = jsonObject.getString("email");
        return Result.success(userService.sendCaptcha(email));
    }
}
