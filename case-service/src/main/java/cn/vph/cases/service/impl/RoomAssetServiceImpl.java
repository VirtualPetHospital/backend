package cn.vph.cases.service.impl;

import cn.vph.cases.entity.Room;
import cn.vph.cases.entity.RoomAsset;
import cn.vph.cases.mapper.RoomAssetMapper;
import cn.vph.cases.mapper.RoomMapper;
import cn.vph.cases.service.RoomAssetService;
import cn.vph.common.CommonErrorCode;
import cn.vph.common.util.AssertUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-20 11:57
 **/
@Service
public class RoomAssetServiceImpl extends ServiceImpl<RoomAssetMapper, RoomAsset> implements RoomAssetService {
    @Autowired
    private RoomAssetMapper roomAssetMapper;
    @Autowired
    private RoomMapper roomMapper;


    @Override
    public Object add(RoomAsset roomAsset) {
        // 是否存在科室
        Room room = roomMapper.selectById(roomAsset.getRoomId());
        AssertUtil.isNotNull(room, CommonErrorCode.ROOM_NOT_EXIST);

        // 查看科室内是否已经有同名设施
        MPJLambdaWrapper<RoomAsset> wrapper = new MPJLambdaWrapper<>();
        wrapper.eq(RoomAsset::getName, roomAsset.getName());
        wrapper.eq(RoomAsset::getRoomId, roomAsset.getRoomId());
        RoomAsset exist = roomAssetMapper.selectOne(wrapper);
        AssertUtil.isNull(exist, CommonErrorCode.ROOM_ASSET_ALREADY_EXIST);

        roomAssetMapper.insert(roomAsset);
        return roomAsset;
    }

    @Override
    public Object delete(Integer roomAssetId) {
        // 科室存在
        Room room = roomMapper.selectById(roomAssetId);
        AssertUtil.isNotNull(room, CommonErrorCode.ROOM_NOT_EXIST);
        // 科室设施存在
        RoomAsset roomAsset = roomAssetMapper.selectById(roomAssetId);
        AssertUtil.isNotNull(roomAsset, CommonErrorCode.ROOM_ASSET_NOT_EXIST);
        roomAssetMapper.deleteById(roomAssetId);
        return null;
    }

    @Override
    public Object get(Integer roomAssetId) {
        // 科室设施存在
        RoomAsset roomAsset = roomAssetMapper.selectById(roomAssetId);
        AssertUtil.isNotNull(roomAsset, CommonErrorCode.ROOM_ASSET_NOT_EXIST);
        return roomAsset;
    }

    // 废弃
    @Override
    public Object list(Integer pageNum, Integer pageSize, String nameKeyword) {
        return null;
    }

    @Override
    public Object listByRoom(Integer roomId) {
        // 科室存在
        Room room = roomMapper.selectById(roomId);
        AssertUtil.isNotNull(room, CommonErrorCode.ROOM_NOT_EXIST);
        MPJLambdaWrapper<RoomAsset> wrapper = new MPJLambdaWrapper<>();
        wrapper.eq(RoomAsset::getRoomId, roomId);
        return roomAssetMapper.selectList(wrapper);
    }

    @Override
    public Object update(RoomAsset roomAsset) {
        // 科室设施存在
        RoomAsset exist = roomAssetMapper.selectById(roomAsset.getRoomAssetId());
        AssertUtil.isNotNull(exist, CommonErrorCode.ROOM_ASSET_NOT_EXIST);
        // 科室存在
        Room room = roomMapper.selectById(roomAsset.getRoomId());
        AssertUtil.isNotNull(room, CommonErrorCode.ROOM_NOT_EXIST);
        // 科室不变
        AssertUtil.isTrue(roomAsset.getRoomId().equals(exist.getRoomId()), CommonErrorCode.CANNOT_CHANGE_ROOM_OF_ROOM_ASSET);
        // 查看科室内是否已经有同名设施
        MPJLambdaWrapper<RoomAsset> wrapper = new MPJLambdaWrapper<>();
        wrapper.eq(RoomAsset::getName, roomAsset.getName());
        wrapper.eq(RoomAsset::getRoomId, roomAsset.getRoomId());
        RoomAsset exist2 = roomAssetMapper.selectOne(wrapper);
        AssertUtil.isNull(exist2, CommonErrorCode.ROOM_ASSET_ALREADY_EXIST);
        roomAssetMapper.updateById(roomAsset);
        return roomAsset;
    }


}
