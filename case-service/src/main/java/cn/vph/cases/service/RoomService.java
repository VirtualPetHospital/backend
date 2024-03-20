package cn.vph.cases.service;

import cn.vph.cases.entity.Room;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-20 11:55
 **/
public interface RoomService {
    Room add(Room room);

    Object delete(Integer roomId);

    IPage<?> list(Integer pageNum, Integer pageSize, String nameKeyword);

    Room get(Integer roomId);

    Room update(Integer roomId, Room room);
}
