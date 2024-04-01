package cn.vph.exam.controller;


import cn.vph.common.controller.BaseController;
import cn.vph.common.Result;
import cn.vph.common.annotation.Student;
import cn.vph.common.annotation.Teacher;
import cn.vph.exam.clients.CategoryFeignClient;
import cn.vph.exam.entity.Question;
import cn.vph.exam.mapper.QuestionMapper;
import cn.vph.exam.service.QuestionService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Caroline
 * @description 题目接口
 * @create 2024/3/15 0:30
 */
@RestController
@RequestMapping(value = "questions")
@Api(value = "QuestionController", tags = {"题目接口"})
public class QuestionController extends BaseController {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CategoryFeignClient categoryFeignClient;

    @Student
    @GetMapping("/{question_id}")
    @ApiOperation(value = "查询单个题目")
    public Result<?> getQuestionById(@PathVariable("question_id") Integer questionId){

        return Result.success(questionService.getQuestionById(questionId));
    }

    @Student
    @GetMapping("")
    @ApiOperation(value = "查询题目列表")
    public Result<?> getQuestionList(
            @RequestParam(value = "page_size", defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(value = "page_num", defaultValue = "1", required = false) Integer pageNum,
            @RequestParam(value = "category_keyword", required = false) String categoryKeyWord,
            @RequestParam(value = "description_keyword", required = false) String descriptionKeyWord
    )
    {
        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        if(categoryKeyWord != null && !categoryKeyWord.isEmpty()){
            List<Integer> categoryIds = categoryFeignClient.getCategoryIds(categoryKeyWord);
            queryWrapper.in(Question::getCategoryId, categoryIds);
        }
        queryWrapper.like(descriptionKeyWord != null && !descriptionKeyWord.isEmpty(), Question::getDescription, descriptionKeyWord);

        IPage<Question> page = new Page<>(pageNum, pageSize);
        return Result.success(super.getData(questionMapper.selectPage(page, queryWrapper)));
    }

    @Teacher
    @PostMapping("")
    @ApiOperation(value = "新增题目")
    public Result<?> addQuestion(@Valid @RequestBody Question question){
        return Result.success(questionService.add(question));
    }

    @Teacher
    @PutMapping("/{question_id}")
    @ApiOperation(value = "更新题目")
    public Result<?> updateQuestion(@PathVariable("question_id") Integer questionId, @Valid @RequestBody Question question){
        question.setQuestionId(questionId);

        return Result.success(questionService.update(question));
    }


    @DeleteMapping("/{question_id}")
    @ApiOperation(value = "删除题目")
    @Teacher
    public Result<?> deleteQuestion(@PathVariable("question_id") Integer questionId){

        questionService.delete(questionId);
        return Result.success();
    }

    @GetMapping("/questions-count/{category_id}")
    @ApiOperation(value = "查询某个病种下的题目数量")
    public Long getQuestionCountByCategoryId(@PathVariable("category_id") Integer categoryId){
        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Question::getCategoryId, categoryId);
        queryWrapper.select(Question::getQuestionId);
        return questionMapper.selectCount(queryWrapper);
    }
}
