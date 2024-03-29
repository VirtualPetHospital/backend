package cn.vph.cases.entity;

import cn.vph.common.validation.VphValidation;
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

    @VphValidation("simpleName")
    private String name;

    @VphValidation("text")
    private String description;

    @VphValidation("fileName")
    private String photo;

    @VphValidation("fileName")
    private String video;

    @TableField("room_id")
    private Integer roomId;
}
