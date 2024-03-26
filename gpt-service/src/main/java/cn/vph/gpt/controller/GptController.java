package cn.vph.gpt.controller;

import cn.vph.common.Result;
import cn.vph.common.annotation.Student;
import cn.vph.gpt.service.GptService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-26 13:27
 **/

@RestController
@RequestMapping("gpts")
@ApiOperation(value = "智能助手", tags = {"智能助手"})
public class GptController {
    @Autowired
    private GptService gptService;

    @Student
    @PostMapping("chat")
    @ApiOperation(value = "发送消息")
public Result<?> sendMsg(@RequestBody String msg) {
        return Result.success(gptService.sendMsg(msg));
    }

}
