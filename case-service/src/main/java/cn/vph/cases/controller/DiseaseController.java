package cn.vph.cases.controller;

import cn.vph.cases.entity.Disease;
import cn.vph.cases.service.DiseaseService;
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
 * @create: 2024-03-19 22:41
 **/
@RestController
@RequestMapping("diseases")
@Api(value = "DiseaseController", tags = {"疾病服务接口"})
public class DiseaseController {
    @Autowired
    private DiseaseService diseaseService;

    @Administrator
    @PostMapping
    @ApiOperation(value = "新增疾病")
    public Result<?> add(@RequestBody Disease disease) {
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
    public Result<?> update(@PathVariable Integer diseaseId, @RequestBody Disease disease) {
        disease.setDiseaseId(diseaseId);
        return Result.success(diseaseService.update(disease));
    }

    @Student
    @GetMapping("{categoryId}")
    @ApiOperation(value = "查询制定病种下的疾病")
    public Result<?> list(@PathVariable Integer categoryId) {
        return Result.success(diseaseService.listByCategory(categoryId));
    }

    @Student
    @GetMapping("{diseaseId}")
    @ApiOperation(value = "查询单个疾病")
    public Result<?> get(@PathVariable Integer diseaseId) {
        return Result.success(diseaseService.get(diseaseId));
    }

}
