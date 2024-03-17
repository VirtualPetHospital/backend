package cn.vph.exam.controller;

import cn.vph.common.Result;
import cn.vph.exam.entity.Question;
import cn.vph.exam.mapper.QuestionMapper;
import cn.vph.exam.service.QuestionService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Caroline
 * @description 题目服务接口
 * @create 2024/3/15 0:30
 */
@RestController
@RequestMapping(value = "/questions")
@Api(value = "QuestionController", tags = {"题目服务接口"})
public class QuestionController extends BaseController {
    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/{question_id}")
    public Result getExamById(@PathVariable("question_id") Integer question_id){
        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Question::getQuestionId, question_id);
        return Result.success(questionMapper.selectList(queryWrapper));
    }

    @GetMapping("/list")
    public Result getExamPages(){
        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        IPage<Question> page = new Page<>(1,10);
        IPage<Question> selectPage = questionMapper.selectPage(page, queryWrapper);
        return Result.success(super.getData(selectPage));
    }

}
