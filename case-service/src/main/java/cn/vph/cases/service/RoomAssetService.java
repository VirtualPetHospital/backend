package cn.vph.cases.service;

import cn.vph.cases.entity.RoomAsset;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-20 11:56
 **/
public interface RoomAssetService {
    Object add(RoomAsset roomAsset);

    Object delete(Integer roomAssetId);

    Object get(Integer roomAssetId);

    Object list(Integer pageNum, Integer pageSize, String nameKeyword);

    Object listByRoom(Integer roomId);

    Object update(RoomAsset roomAsset);
}
