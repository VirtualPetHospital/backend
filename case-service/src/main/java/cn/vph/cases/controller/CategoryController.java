package cn.vph.cases.controller;

import cn.vph.cases.entity.Category;
import cn.vph.cases.mapper.CategoryMapper;
import cn.vph.cases.service.CategoryService;
import cn.vph.common.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
public class CategoryController extends BaseController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryMapper categoryMapper;

    @GetMapping("")
    @ApiOperation(
            value = "根据关键词查询病种列表"
    )
    public Result<?> getCategoryList(
            @RequestParam(value = "name_keyword", required = false) String keyword
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
        queryWrapper.like(keyword != null && !keyword.isEmpty(), Category::getName, keyword);

        return categoryMapper.selectList(queryWrapper).stream().map(Category::getCategoryId).collect(Collectors.toList());
    }
}
