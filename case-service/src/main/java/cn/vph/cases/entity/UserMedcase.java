package cn.vph.cases.entity;

import cn.vph.common.validation.VphValidation;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Caroline
 * @description 用户病例实体类
 * @create 2024/3/17 0:52
 */
@Data
@TableName("user_medcase")
public class UserMedcase implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField(value = "user_id")
    private Integer userId;

    @TableField(value = "medcase_id")
    private Integer medcaseId;


//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("view_time")
    @VphValidation("time")
    private LocalDateTime viewTime;
}
