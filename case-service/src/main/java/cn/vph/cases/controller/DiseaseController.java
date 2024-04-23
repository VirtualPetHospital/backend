package cn.vph.cases.controller;

import cn.vph.cases.entity.Disease;
import cn.vph.cases.service.DiseaseService;
import cn.vph.common.CommonConstant;
import cn.vph.common.Result;
import cn.vph.common.annotation.Administrator;
import cn.vph.common.annotation.Student;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-19 22:41
 **/
@RestController
@RequestMapping("diseases")
@Api(value = "DiseaseController", tags = {"疾病服务接口"})
public class DiseaseController {
    @Autowired
    private DiseaseService diseaseService;
    private void setDefaultPhoto(Disease disease){
        if(disease.getPhoto() == null){
            disease.setPhoto(CommonConstant.DEFAULT_IMAGE);
        }
    }

    @PostMapping
    @Administrator
    @ApiOperation(value = "新增疾病")
    public Result<?> add(@Valid @RequestBody Disease disease) {
        setDefaultPhoto(disease);
        return Result.success(diseaseService.add(disease));
    }

    @Administrator
    @DeleteMapping("{diseaseId}")
    @ApiOperation(value = "删除疾病")
    public Result<?> delete(@PathVariable Integer diseaseId) {
        return Result.success(diseaseService.delete(diseaseId));
    }

    @Administrator
    @PutMapping("{diseaseId}")
    @ApiOperation(value = "更新疾病")
    public Result<?> update(@PathVariable Integer diseaseId,@Valid @RequestBody Disease disease) {
        setDefaultPhoto(disease);
        disease.setDiseaseId(diseaseId);
        return Result.success(diseaseService.update(disease));
    }

    @Student
    @GetMapping("/categories/{category_id}")
    @ApiOperation(value = "查询指定病种下的疾病")
    public Result<?> list(@PathVariable(value = "category_id") Integer categoryId) {
        return Result.success(diseaseService.listByCategory(categoryId));
    }

    @Student
    @GetMapping("{diseaseId}")
    @ApiOperation(value = "查询单个疾病")
    public Result<?> getDiseaseById(@PathVariable Integer diseaseId) {
        return Result.success(diseaseService.getDiseaseById(diseaseId));
    }

}
