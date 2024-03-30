package cn.vph.exam.controller;

import cn.vph.common.Result;
import cn.vph.common.annotation.Student;
import cn.vph.exam.entity.AnswerSheet;
import cn.vph.exam.service.AnswerSheetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Caroline
 * @description 答题卡接口
 * @create 2024/3/19 19:57
 */
@RestController
@RequestMapping(value = "answer-sheets")
@Api(value = "AnswerSheetController", tags = {"答题卡接口"})
public class AnswerSheetController{

    @Autowired
    private AnswerSheetService answerSheetService;

    /**
     * 新增答题卡
     * PRE：
     *  1. 检查是否报名 --> participant 表 participated = False
     * POST:
     *  1. participant 表 participated = True
     * @param answerSheet
     * @return
     */
    @Student
    @PostMapping("")
    @ApiOperation(value = "新增答题卡")
    public Result<?> add( @RequestBody AnswerSheet answerSheet)
    {
        return Result.success(answerSheetService.add(answerSheet));
    }


    @Student
    @PutMapping("/{answer_sheet_id}")
    @ApiOperation(value = "更新答题卡")
    public Result<?> update(@PathVariable(value = "answer_sheet_id") Integer answerSheetId, @RequestBody AnswerSheet answerSheet)
    {
        return Result.success(answerSheetService.update(answerSheetId, answerSheet));
    }

    @Student
    @GetMapping("/{exam_id}")
    @ApiOperation(value = "查询单个试卷的答题卡")
    public Result<?> getAnswerSheetById(@PathVariable(value = "exam_id") Integer examId)
    {
        return Result.success(answerSheetService.getAnswerSheetByExamId(examId));
    }

}
