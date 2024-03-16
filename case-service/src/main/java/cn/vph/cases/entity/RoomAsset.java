package cn.vph.cases.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Caroline
 * @description 科室设施实体类
 * @create 2024/3/17 0:47
 */
@Data
@TableName("room_asset")
public class RoomAsset implements Serializable {

    @TableId(value = "room_asset_id", type = IdType.AUTO)
    private Integer roomAssetId;

    private String name;

    private String description;

    private String photo;

    private String video;

    @TableField("room_id")
    private Integer roomId;
}
