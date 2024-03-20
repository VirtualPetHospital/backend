package cn.vph.cases.controller;

import cn.vph.cases.entity.RoomAsset;
import cn.vph.cases.service.RoomAssetService;
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
 * @create: 2024-03-20 11:55
 **/
@RestController
@RequestMapping("/room-assets")
@Api(value = "RoomAssetController", tags = "科室设施服务接口")
public class RoomAssetController {
    @Autowired
    private RoomAssetService roomAssetService;

    @Administrator
    @PostMapping
    @ApiOperation(value = "新增科室设施")
    public Result<?> add(@RequestBody RoomAsset roomAsset) {
        return Result.success(roomAssetService.add(roomAsset));
    }

    @Administrator
    @DeleteMapping("{roomAssetId}")
    @ApiOperation(value = "删除科室设施")
    public Result<?> delete(@RequestParam Integer roomAssetId) {
        return Result.success(roomAssetService.delete(roomAssetId));
    }

    @Student
    @GetMapping("{roomAssetId}")
    @ApiOperation(value = "查询单个科室设施")
    public Result<?> get(@PathVariable Integer roomAssetId) {
        return Result.success(roomAssetService.get(roomAssetId));
    }


    @Student
    @GetMapping("/rooms/{roomId}")
    @ApiOperation(value = "查询指定room下的科室设施")
    public Result<?> list(
            @PathVariable Integer roomId
    ) {
        return Result.success(roomAssetService.listByRoom(roomId));
    }

    @Administrator
    @PutMapping("{roomAssetId}")
    @ApiOperation(value = "更新科室设施")
    public Result<?> update(@PathVariable Integer roomAssetId, @RequestBody RoomAsset roomAsset) {
        roomAsset.setRoomAssetId(roomAssetId);
        return Result.success(roomAssetService.update(roomAsset));
    }

//    @Student
//    @GetMapping("/list")
//    @ApiOperation(value = "查询科室设施列表")
//public Result<?> list(
//            @RequestParam(value = "page_num", defaultValue = "1") Integer pageNum,
//            @RequestParam(value = "page_size", defaultValue = "10") Integer pageSize,
//            @RequestParam(value = "name_keyword", required = false) String nameKeyword
//    ) {
//        return Result.success(roomAssetService.list(pageNum, pageSize, nameKeyword));
//    }

}



