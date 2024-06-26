package cn.vph.cases.controller;

import cn.vph.cases.entity.Medcase;
import cn.vph.cases.service.MedcaseService;
import cn.vph.common.CommonConstant;
import cn.vph.common.Result;
import cn.vph.common.annotation.Administrator;
import cn.vph.common.annotation.Student;
import cn.vph.common.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("medcases")
@Api(value = "MedcaseController", tags = {"病例服务接口"})
public class MedcaseController extends BaseController {

    @Autowired
    private MedcaseService medcaseService;

    private void setDefaultPhoto(Medcase medcase) {
        if (medcase.getInfoPhoto() == null) {
            medcase.setInfoPhoto(CommonConstant.DEFAULT_IMAGE);
        }
    }
    @Student
    @GetMapping("/{medcase_id}")
    @ApiOperation(value = "查询单个病例")
    public Result<?> getMedcaseById(@PathVariable(value = "medcase_id") Integer medcaseId) {
        return Result.success(medcaseService.getMedcaseById(medcaseId));
    }

    @Student
    @GetMapping("list")
    @ApiOperation(value = "查询病例列表-分页")
    public Result<?> getMedcaseList(
            @RequestParam(value = "page_size", defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(value = "page_num", defaultValue = "1", required = false) Integer pageNum,
            @RequestParam(value = "info_keyword", required = false) String infoKeyword,
            @RequestParam(value = "name_keyword", required = false) String nameKeyword,
            @RequestParam(value = "disease_name", required = false) String diseaseName
    ) {
        return Result.success(super.getData(medcaseService.getMedcaseList(pageSize, pageNum, infoKeyword, nameKeyword, diseaseName)));
    }

    @Student
    @GetMapping
    @ApiOperation(value = "查询病例列表")
    public Result<?> getAllMedcaseList(
            @RequestParam(value = "info_keyword", required = false) String infoKeyword,
            @RequestParam(value = "name_keyword", required = false) String nameKeyword,
            @RequestParam(value = "disease_name", required = false) String diseaseName
    ) {
        return Result.success(super.getData(medcaseService.getAllMedcaseList(infoKeyword, nameKeyword, diseaseName)));
    }

    @Administrator
    @PostMapping
    @ApiOperation(value = "新增病例")
    public Result<?> addMedcase(@Valid @RequestBody Medcase medcase) {
        setDefaultPhoto(medcase);
        return Result.success(medcaseService.add(medcase));
    }

    @Administrator
    @PutMapping("/{medcase_id}")
    @ApiOperation(value = "更新病例")
    public Result<?> updateMedcase(@PathVariable(value = "medcase_id") Integer medcaseId, @Valid @RequestBody Medcase medcase) {
        setDefaultPhoto(medcase);
        medcase.setMedcaseId(medcaseId);
        return Result.success(medcaseService.update(medcase));
    }

    @Administrator
    @DeleteMapping("/{medcase_id}")
    @ApiOperation(value = "删除病例")
    public Result<?> deleteMedcase(@PathVariable(value = "medcase_id") Integer medcaseId) {

        medcaseService.delete(medcaseId);
        return Result.success();
    }


}
