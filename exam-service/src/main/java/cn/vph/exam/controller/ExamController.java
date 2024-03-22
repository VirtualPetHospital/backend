package cn.vph.exam.controller;

import cn.vph.common.controller.BaseController;
import cn.vph.common.Result;
import cn.vph.common.annotation.Student;
import cn.vph.common.annotation.Teacher;
import cn.vph.exam.entity.Exam;
import cn.vph.exam.service.ExamService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: vph-backend
 * @description: 考试服务接口
 * @author: Caroline
 * @create: 2024-03-17 18:10
 **/
@RestController
@RequestMapping(value = "exams")
@Api(value = "ExamController", tags = {"考试接口"})
public class ExamController extends BaseController {

    @Autowired
    private ExamService examService;


    @Student
    @GetMapping("/{exam_id}")
    @ApiOperation(value = "查询单个考试")
    public Result<?> getExamById(@PathVariable(value = "exam_id") Integer examId)
    {
        return Result.success(examService.getExamById(examId));
    }

    /**
     *
     * @param pageSize
     * @param pageNum
     * @param nameKeyword     考试名称关键字
     * @param level           考试等级
     * @param sortByStartTime 默认1升序，2降序，不给这个字段就不针对时间排序
     * @param participated    true是已经报名的，false是没报名的
     * @return Result
     */
    @Student
    @GetMapping("")
    @ApiOperation(value = "查询考试列表")
    public Result<?> getExamList(
            @RequestParam(value = "page_size", defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(value = "page_num", defaultValue = "1", required = false) Integer pageNum,
            @RequestParam(value = "name_keyword", required = false) String nameKeyword,
            @RequestParam(value = "level", required = false) Integer level,
            @RequestParam(value = "sort_by_start_time", defaultValue = "1", required = false) Integer sortByStartTime,
            @RequestParam(value = "participated", required = false) Boolean participated
    )
    {
        return Result.success(super.getData(examService.getExamList(pageSize, pageNum, nameKeyword, level, sortByStartTime, participated)));
    }


    @Teacher
    @PostMapping("")
    @ApiOperation(value = "新增考试")
    public Result<?> addExam(@RequestBody Exam exam)
    {
        return Result.success(examService.add(exam));
    }

    @Teacher
    @PutMapping("/{exam_id}")
    @ApiOperation(value = "更新考试")
    public Result<?> updateExam(@PathVariable("exam_id") Integer examId, @RequestBody Exam exam)
    {
        exam.setExamId(examId);
        return Result.success(examService.update(exam));
    }

    @Teacher
    @DeleteMapping("/{exam_id}")
    @ApiOperation(value = "删除考试")
    public Result<?> deleteExam(@PathVariable("exam_id") Integer examId)
    {
        examService.delete(examId);
        return Result.success();
    }

    /**
     * 报名考试, 暂时只有学生可以报名
     * @param examId
     * @return
     */
    @Student
    @PostMapping("/enroll/{exam_id}")
    @ApiOperation(value = "报名考试")
    public Result<?> enrollExam(@PathVariable("exam_id") Integer examId)
    {
        examService.enroll(examId);
        return Result.success("报名考试成功", null);
    }

    @Student
    @PostMapping("/cancel-enroll/{exam_id}")
    @ApiOperation(value = "取消报名考试")
    public Result<?> unEnrollExam(@PathVariable("exam_id") Integer examId)
    {
        examService.unEnroll(examId);
        return Result.success("取消报名考试成功", null);
    }
}
