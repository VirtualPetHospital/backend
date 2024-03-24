package cn.vph.cases.controller;

import cn.vph.common.controller.BaseController;
import cn.vph.cases.entity.Medicine;
import cn.vph.cases.service.MedicineService;
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
 * @create: 2024-03-19 22:55
 **/
@RestController
@RequestMapping("medicines")
@Api(value = "MedicineController", tags = {"药品服务接口"})
public class MedicineController extends BaseController {
    @Autowired
    private MedicineService medicineService;

    @Administrator
    @PostMapping
    @ApiOperation(value = "新增药品")
    public Result<?> add(@RequestBody Medicine medicine) {
        return Result.success(medicineService.add(medicine));
    }

    @Administrator
    @PutMapping("{medicineId}")
    @ApiOperation(value = "更新药品")
    public Result<?> update(@PathVariable Integer medicineId, @RequestBody Medicine medicine) {
        medicine.setMedicineId(medicineId);
        return Result.success(medicineService.update(medicine));
    }

    @Student
    @GetMapping
    @ApiOperation(value = "查询药品列表")
    public Result<?> list(
            @RequestParam(value = "page_num", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "page_size", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "name_keyword", required = false) String nameKeyword
    ) {
        return Result.success(super.getData(medicineService.list(pageNum, pageSize, nameKeyword)));
    }

    @Administrator
    @GetMapping("{medicineId}")
    @ApiOperation(value = "查询单个药品")
    public Result<?> get(@PathVariable Integer medicineId) {
        return Result.success(medicineService.get(medicineId));
    }
    @Administrator
    @DeleteMapping("{medicineId}")
    @ApiOperation(value = "删除药品")
    public Result<?> delete(@PathVariable Integer medicineId) {
        return Result.success(medicineService.delete(medicineId));
    }
}
