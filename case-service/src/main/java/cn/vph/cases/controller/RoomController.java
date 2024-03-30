package cn.vph.cases.controller;

import cn.vph.cases.entity.Room;
import cn.vph.cases.service.RoomService;
import cn.vph.common.Result;
import cn.vph.common.annotation.Administrator;
import cn.vph.common.annotation.Student;
import cn.vph.common.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-20 11:55
 **/
@RestController
@RequestMapping("/rooms")
@Api(value = "RoomController", tags = "科室服务接口")
public class RoomController extends BaseController {

    @Autowired
    private RoomService roomService;

    @Administrator
    @PostMapping
    @ApiOperation(value = "新增科室")
    public Result<Room> add(@Valid @RequestBody Room room) {
        return Result.success(roomService.add(room));
    }

    @Administrator
    @DeleteMapping("{roomId}")
    @ApiOperation(value = "删除科室")
    public Result<?> delete(@PathVariable Integer roomId) {
        return Result.success(roomService.delete(roomId));
    }

    @Student
    @GetMapping("")
    @ApiOperation(value = "查询科室列表")
    public Result<?> list(
            @RequestParam(value = "page_num", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "page_size", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "name_keyword", required = false) String nameKeyword
    ) {
        return Result.success(super.getData(roomService.list(pageNum, pageSize, nameKeyword)));
    }

    @Student
    @GetMapping("{roomId}")
    @ApiOperation(value = "查询单个科室")
    public Result<Room> get(@PathVariable Integer roomId) {
        return Result.success(roomService.get(roomId));
    }

    @Administrator
    @PutMapping("{roomId}")
    @ApiOperation(value = "更新科室")
    public Result<Room> update(@Valid @PathVariable Integer roomId, @RequestBody Room room) {
        return Result.success(roomService.update(roomId, room));
    }
}
