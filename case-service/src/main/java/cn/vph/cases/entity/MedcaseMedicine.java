package cn.vph.cases.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Caroline
 * @description 病例 药品 多对多关系表 实体类
 * @create 2024/3/17 0:41
 */
@Data
@TableName("medcase_medicine")
public class MedcaseMedicine implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer num;

    @TableField(value = "medcase_id")
    private Integer medcaseId;

    @TableField(value = "medicine_id")
    private Integer medicineId;
}
