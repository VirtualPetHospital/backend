package cn.vph.exam.controller;

import cn.vph.exam.service.ExamService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/exams")
@Api(value = "ExamController", tags = {"试卷服务接口"})
public class ExamController {

    @Autowired
    private ExamService examService;


}
