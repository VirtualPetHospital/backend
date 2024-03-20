package cn.vph.cases.service.impl;

import cn.vph.cases.entity.Room;
import cn.vph.cases.entity.RoomAsset;
import cn.vph.cases.mapper.RoomAssetMapper;
import cn.vph.cases.mapper.RoomMapper;
import cn.vph.cases.service.RoomService;
import cn.vph.common.CommonErrorCode;
import cn.vph.common.util.AssertUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-20 11:56
 **/
@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private RoomAssetMapper roomAssetMapper;

    public Room selectByName(String name) {
        MPJLambdaWrapper<Room> wrapper = new MPJLambdaWrapper<>();
        wrapper.eq(Room::getName, name);
        return roomMapper.selectOne(wrapper);
    }


    @Override
    public Room add(Room room) {
        // 是否存在
        Room exist = selectByName(room.getName());
        AssertUtil.isNull(exist, CommonErrorCode.ROOM_ALREADY_EXIST);
        roomMapper.insert(room);
        return room;
    }

    @Override
    public Object delete(Integer roomId) {
        // 外键Delete Cascade 科室内设施均被删除
        // 是否存在
        Room exist = roomMapper.selectById(roomId);
        AssertUtil.isNotNull(exist, CommonErrorCode.ROOM_NOT_EXIST);
        roomMapper.deleteById(roomId);

        return null;
    }

    @Override
    public IPage<?> list(Integer pageNum, Integer pageSize, String nameKeyword) {
        MPJLambdaWrapper<Room> wrapper = new MPJLambdaWrapper<>();
        if (nameKeyword != null && !nameKeyword.isEmpty()) {
            wrapper.like(Room::getName, nameKeyword);
        }
        return roomMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public Room get(Integer roomId) {
        Room room = roomMapper.selectById(roomId);
        AssertUtil.isNotNull(room, CommonErrorCode.ROOM_NOT_EXIST);
        // 查询科室内的科室设施列表
        MPJLambdaWrapper<RoomAsset> wrapper = new MPJLambdaWrapper<>();
        wrapper.eq(RoomAsset::getRoomId, roomId);
        room.setRoomAssets(roomAssetMapper.selectList(wrapper));
        return room;
    }

    @Override
    public Room update(Integer roomId, Room room) {
        // 存在科室
        Room exist = roomMapper.selectById(roomId);
        AssertUtil.isNotNull(exist, CommonErrorCode.ROOM_NOT_EXIST);
        // 更新的科室名称是否已经存在
        Room existName = selectByName(room.getName());
        AssertUtil.isTrue(existName == null || existName.getRoomId().equals(roomId), CommonErrorCode.ROOM_ALREADY_EXIST);

        room.setRoomId(roomId);
        // 查询科室内的科室设施是否都存在
        if (room.getRoomAssets() != null) {
            room.getRoomAssets().forEach(roomAsset -> {
                AssertUtil.isNotNull(roomAssetMapper.selectById(roomAsset.getRoomAssetId()), CommonErrorCode.ROOM_ASSET_NOT_EXIST);
            });
        }
        roomMapper.updateById(room);
        return room;
    }
}
