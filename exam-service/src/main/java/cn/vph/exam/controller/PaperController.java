package cn.vph.exam.controller;

import cn.vph.common.Result;
import cn.vph.common.annotation.Student;
import cn.vph.common.annotation.Teacher;
import cn.vph.exam.entity.Paper;
import cn.vph.exam.mapper.PaperMapper;
import cn.vph.exam.service.PaperService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Caroline
 * @description 试卷接口
 * @create 2024/3/17 21:06
 */
@RestController
@RequestMapping(value = "papers")
@Api(value = "PaperController", tags = {"试卷接口"})
public class PaperController extends BaseController {

    @Autowired
    private PaperService paperService;

    @Autowired
    private PaperMapper paperMapper;


    @Student
    @GetMapping("/{paper_id}")
    @ApiOperation(value = "查询单个试卷")
    public Result<?> getPaperById(@PathVariable("paper_id") Integer paperId){
        /**
         * 不直接用 paperMapper.selectById(paperId) 因为可能不存在 需要返回对应的msg
         */
        return Result.success(paperService.getPaperById(paperId));
    }

    @Student
    @GetMapping("")
    @ApiOperation(value = "查询试卷列表")
    public Result<?> getPaperList(
            @RequestParam(value = "page_size", defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(value = "page_num", defaultValue = "1", required = false) Integer pageNum,
            @RequestParam(value = "name", required = false) String nameKeyWord
    )
    {
        LambdaQueryWrapper<Paper> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(nameKeyWord != null && !nameKeyWord.isEmpty(), Paper::getName, nameKeyWord);
        IPage<Paper> page = new Page<>(pageNum, pageSize);
        return Result.success(super.getData(paperMapper.selectPage(page, queryWrapper)));
    }


    @Teacher
    @PostMapping("")
    @ApiOperation(value = "新增试卷")
    public Result<?> addPaper(@RequestBody Paper paper){
        return Result.success(paperService.add(paper));
    }

    @Teacher
    @PutMapping("/{paper_id}")
    @ApiOperation(value = "更新试卷")
    public Result<?> updatePaper(@PathVariable("paper_id") Integer paperId, @RequestBody Paper paper){
        paper.setPaperId(paperId);
        return Result.success(paperService.update(paper));
    }

    @Teacher
    @DeleteMapping("/{paper_id}")
    @ApiOperation(value = "删除试卷")
    public Result<?> deletePaper(@PathVariable("paper_id") Integer paperId){
        paperService.delete(paperId);
        return Result.success();
    }
}
