package cn.vph.cases.controller;

import cn.vph.common.CommonConstant;
import cn.vph.common.controller.BaseController;
import cn.vph.cases.entity.Operation;
import cn.vph.cases.service.OperationService;
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
 * @create: 2024-03-18 22:26
 **/

@RestController
@RequestMapping("operations")
@Api(value = "OperationController", tags = {"手术服务接口"})
public class OperationController extends BaseController{

    @Autowired
    private OperationService operationService;

    private void setDefaultPhoto(Operation operation) {
        if (operation.getPhoto() == null) {
            operation.setPhoto(CommonConstant.DEFAULT_IMAGE);
        }
    }
    @Administrator
    @PostMapping
    @ApiOperation(value = "新增手术")
    public Result<Operation> add(@Valid @RequestBody Operation operation) {
        setDefaultPhoto(operation);
        return Result.success(operationService.add(operation));
    }

    @Administrator
    @DeleteMapping("{operationId}")
    @ApiOperation(value = "删除手术")
    public Result<?> delete(@PathVariable Integer operationId) {
        return Result.success(operationService.delete(operationId));
    }

    @Administrator
    @PutMapping("{operationId}")
    @ApiOperation(value = "更新手术")
    public Result<Operation> update(@PathVariable Integer operationId, @Valid @RequestBody Operation operation) {
        setDefaultPhoto(operation);
        operation.setOperationId(operationId);
        return Result.success(operationService.update(operation));
    }

    @Student
    @GetMapping("{operationId}")
    @ApiOperation(value = "查询单个手术")
    public Result<Operation> get(@PathVariable Integer operationId) {
        return Result.success(operationService.get(operationId));
    }

    @Student
    @GetMapping("")
    @ApiOperation(value = "查询手术列表")
    public Result<?> list(
            @RequestParam(value = "page_num", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "page_size", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "name_keyword", required = false) String nameKeyword
    ) {
        return Result.success(super.getData(operationService.list(pageNum, pageSize, nameKeyword)));
    }

}
