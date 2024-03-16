package cn.vph.cases.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

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

    @TableField(value = "view_time")
    private Date viewTime;
}
