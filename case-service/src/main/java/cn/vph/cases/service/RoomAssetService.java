package cn.vph.cases.service;

import cn.vph.cases.entity.RoomAsset;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-20 11:56
 **/
public interface RoomAssetService extends IService<RoomAsset> {
    Object add(RoomAsset roomAsset);

    Object delete(Integer roomAssetId);

    Object get(Integer roomAssetId);

    Object list(Integer pageNum, Integer pageSize, String nameKeyword);

    Object listByRoom(Integer roomId);

    Object update(RoomAsset roomAsset);
}
