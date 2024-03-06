/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ecnu.vphbackend.controller;

import com.ecnu.vphbackend.common.CommonErrorCode;
import com.ecnu.vphbackend.common.CommonException;
import com.ecnu.vphbackend.common.Result;
import com.ecnu.vphbackend.demos.web.User;
import com.ecnu.vphbackend.mapper.TestMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Api(tags = "测试")
@Controller
public class BasicController {
    @Autowired
    private TestMapper testMapper;

    // http://127.0.0.1:8080/hello?name=lisi
    @ApiOperation(value = "获取用户信息")
    @RequestMapping("/hello")
    @ResponseBody
    public String hello(@RequestParam(name = "name", defaultValue = "unknown user") String name) {
        return "Hello " + name;
    }

    // http://127.0.0.1:8080/user
    @RequestMapping("/user")
    @ResponseBody
    public User user() {
        User user = new User();
        user.setName("theonefx");
        user.setAge(666);
        return user;
    }

    // http://127.0.0.1:8080/save_user?name=newName&age=11
    @RequestMapping("/save_user")
    @ResponseBody
    public String saveUser(User u) {
        return "user will save: name=" + u.getName() + ", age=" + u.getAge();
    }

    // http://127.0.0.1:8080/html
    @RequestMapping("/html")
    public String html() {
        return "index.html";
    }

    @ModelAttribute
    public void parseUser(@RequestParam(name = "name", defaultValue = "unknown user") String name
            , @RequestParam(name = "age", defaultValue = "12") Integer age, User user) {
        user.setName("zhangsan");
        user.setAge(18);
    }

    @GetMapping("/test_mysql")
    @ResponseBody
    public Integer getCount() {
        return testMapper.getCount();
    }

    @ResponseBody
    @GetMapping("/test_result")
    public Result<String> testResult() {
        return Result.success("成功");
    }

    @ResponseBody
    @GetMapping("/test_common_exception")
    public Result<String> testCommonException() {
        throw new CommonException(CommonErrorCode.USER_NOT_LOGGED_IN);
    }

    @ResponseBody
    @GetMapping("/test_non_common_exception")
    public Result<String> testException() throws Exception {
        throw new Exception("test");
    }
}
