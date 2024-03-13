package cn.vph.cases.controller;

import cn.vph.common.CommonErrorCode;
import cn.vph.common.CommonException;
import cn.vph.common.Result;

import cn.vph.cases.service.CaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("cases")
public class CaseController {
    @Autowired
    private CaseService caseService;

    /**
     * 测试Controller是否可用
     */
    @RequestMapping("test")
    public Result<String> test() {
        // 测试Result.success方法
        return Result.success("test success");

    }

    /**
     * 测试抛出异常
     */
    @RequestMapping("exception")
    public Result<String> exception() {
        // 抛出CommonException
        throw new CommonException(CommonErrorCode.USER_NOT_LOGGED_IN);
    }

}
