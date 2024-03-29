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
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @Student
    @GetMapping("me")
    @ApiOperation(value = "获取当前用户信息")
    public Result<User> me() {
        return Result.success(userService.me());
    }

    @PostMapping
    @ApiOperation(value = "新增用户")
    public Result<User> register(@Valid @RequestBody RegisterRequest registerRequest) {
        return Result.success(userService.register(registerRequest));
    }


    @Administrator
    @DeleteMapping("{userId}")
    @ApiOperation(value = "删除用户")
    public Result<Object> delete(@PathVariable Integer userId) {
        return Result.success(userService.delete(userId));
    }

    @Student
    @PutMapping
    @ApiOperation(value = "更新用户信息")
    public Result<User> update(@Valid @RequestBody User user) {
        return Result.success(userService.update(user));
    }

    @PostMapping("login")
    @ApiOperation(value = "用户登录")
    public Result<User> login(@RequestBody User user) {
        return Result.success(userService.login(user.getNickname(), user.getPassword()));
    }

    @Student
    @PostMapping("logout")
    @ApiOperation(value = "用户登出")
    public Result<Object> logout() {
        return Result.success(userService.logout());
    }

    @GetMapping("nickname")
    @ApiOperation(value = "检查用户名是否可用")
    public Result<Boolean> checkNickname(@NotNull @RequestParam String nickname) {
        return Result.success(userService.checkNickname(nickname));
    }

    @GetMapping("email")
    @ApiOperation(value = "检查邮箱是否可用")
    public Result<Boolean> checkEmail(@RequestParam String email) {
        return Result.success(userService.checkEmail(email));
    }

    @Administrator

    @GetMapping("list")
    @ApiOperation(value = "查询用户列表")
    public Result<?> list(
            @RequestParam(value = "page_num", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "page_size", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "nickname_keyword", required = false) String nicknameKeyword,
            @RequestParam(value = "sort_by_nickname", required = false) Integer sortByNickname
    ) {
        return Result.success(super.getData(userService.list(pageNum, pageSize, type, nicknameKeyword, sortByNickname)));
    }

    @Student
    @GetMapping("medcases")
    @ApiOperation(value = "查询用户查看过的病例")
    public Result<?> medcases(
            @RequestParam(value = "page_num", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "page_size", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "medcase_info_keyword", required = false) String medcaseInfoKeyword,
            @RequestParam(value = "medcase_name_keyword", required = false) String medcaseNameKeyword,
            @RequestParam(value = "disease", required = false) String disease,
            @RequestParam(value = "sort_by_view_time", required = false) Integer sortByViewTime
    ) {
        return Result.success(super.getData(userService.medcases(pageNum, pageSize, medcaseInfoKeyword, medcaseNameKeyword, disease, sortByViewTime)));
    }


    @PostMapping("captcha")
    @ApiOperation(value = "请求验证码")
    public Result<Object> captcha(@RequestBody JSONObject jsonObject) {
        String email = jsonObject.getString("email");
        return Result.success(userService.sendCaptcha(email));
    }
}
