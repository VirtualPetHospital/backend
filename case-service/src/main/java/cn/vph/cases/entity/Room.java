package cn.vph.cases.entity;

import cn.vph.common.validation.VphValidation;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Caroline
 * @description 科室实体类
 * @create 2024/3/17 0:45
 */
@Data
@TableName("room")
public class Room implements Serializable {

    @TableId(value = "room_id", type = IdType.AUTO)
    private Integer roomId;

    @VphValidation("text")
    private String description;

    @VphValidation("simpleName")
    private String name;

    @VphValidation("fileName")
    private String photo;

    @TableField(exist = false)
    private List<RoomAsset> roomAssets;
}
