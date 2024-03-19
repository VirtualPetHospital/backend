package cn.vph.cases.controller;

import cn.vph.cases.entity.Category;
import cn.vph.cases.service.CategoryService;
import cn.vph.common.Result;
import cn.vph.common.annotation.Administrator;
import cn.vph.common.annotation.Student;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-18 22:52
 **/
@RestController
@RequestMapping("categories")
@Api(value = "CategoryController", tags = {"病种服务接口"})
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

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

}
