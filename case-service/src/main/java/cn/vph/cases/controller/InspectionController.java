package cn.vph.cases.controller;

import cn.vph.common.controller.BaseController;
import cn.vph.cases.entity.Inspection;
import cn.vph.cases.service.InspectionService;
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
 * @create: 2024-03-20 08:48
 **/
@RestController
@RequestMapping("inspections")
@Api(value = "InspectionController", tags = {"检查项目接口"})
public class InspectionController extends BaseController {

    @Autowired
    private InspectionService inspectionService;

    @Administrator
    @PostMapping
    @ApiOperation(value = "新增检查项目")
    public Result<?> add(@RequestBody Inspection inspection) {
        return Result.success(inspectionService.add(inspection));
    }

    @Administrator
    @PutMapping("{inspectionId}")
    @ApiOperation(value = "更新检查项目")
    public Result<?> update(@PathVariable Integer inspectionId, @RequestBody Inspection inspection) {
        inspection.setInspectionId(inspectionId);
        return Result.success(inspectionService.update(inspection));
    }

    @Student
    @GetMapping("")
    @ApiOperation(value = "查询检查项目列表")
    public Result<?> list(
            @RequestParam(value = "page_num", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "page_size", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "name_keyword", required = false) String nameKeyword
    ) {
        return Result.success(super.getData(inspectionService.list(pageNum, pageSize, nameKeyword)));
    }

    @Student
    @GetMapping("{inspectionId}")
    @ApiOperation(value = "查询单个检查项目")
    public Result<?> get(@PathVariable Integer inspectionId) {
        return Result.success(inspectionService.get(inspectionId));
    }

    @Administrator
    @DeleteMapping("{inspectionId}")
    @ApiOperation(value = "删除检查项目")
    public Result<?> delete(@PathVariable Integer inspectionId) {
        return Result.success(inspectionService.delete(inspectionId));
    }

}
