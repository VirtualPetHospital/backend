package cn.vph.cases.controller;

import cn.vph.cases.entity.Category;
import cn.vph.cases.mapper.CategoryMapper;
import cn.vph.cases.service.CategoryService;
import cn.vph.common.Result;
import cn.vph.common.annotation.Administrator;
import cn.vph.common.annotation.Student;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Caroline
 * @description 病种服务接口
 * @create 2024/3/17 10:55
 */

@RestController
@RequestMapping("categories")
@Api(value = "CategoryController", tags = {"病种服务接口"})
public class CategoryController extends BaseController{

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryMapper categoryMapper;

    @Administrator
    @PostMapping
    @ApiOperation(value = "新增病种")
    public Result<?> add(@RequestBody Category category){
        return Result.success(categoryService.add(category));
    }

    @Administrator
    @PutMapping("{categoryId}")
    @ApiOperation(value = "更新病种")
    public Result<?> update(@PathVariable  Integer categoryId,@RequestBody Category category){
        category.setCategoryId(categoryId);
        return Result.success(categoryService.update(category));
    }
    @Administrator
    @DeleteMapping("{categoryId}")
    @ApiOperation(value = "删除病种")
    public Result<?> delete(@PathVariable Integer categoryId){
        return Result.success(categoryService.delete(categoryId));
    }

    @Student
    @GetMapping("list")
    @ApiOperation(value = "查询所有病种")
    public Result<?> list(){
        return Result.success(categoryService.list());
    }

    @GetMapping("")
    @ApiOperation(value = "根据关键词查询病种列表")
    public Result<?> getCategoryList(
            @RequestParam(value = "name_keyword") String keyword
    ){
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        if(keyword != null && !keyword.isEmpty()){
            queryWrapper.like(Category::getName, keyword);
        }
        return Result.success(categoryMapper.selectList(queryWrapper));
    }


    @GetMapping("/keyword-ids")
    @ApiOperation(
            value = "根据关键词查询病种id列表",
            notes = "提供给其他服务调用"
    )
    public List<Integer> getCategoryIds(
            @RequestParam(value = "category_keyword") String keyword
    )
    {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();

        if(keyword != null && !keyword.isEmpty()){
            queryWrapper.like(Category::getName, keyword);
        }
        return categoryMapper.selectList(queryWrapper).stream().map(Category::getCategoryId).collect(Collectors.toList());
    }
}



