package cn.vph.cases.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Caroline
 * @description 病例检查项多对多关系表实体类
 * @create 2024/3/17 0:39
 */
@Data
@TableName("medcase_inspection")
public class MedcaseInspection implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String value;

    @TableField(value = "medcase_id")
    private Integer medcaseId;

    @TableField(value = "inspection_id")
    private Integer inspectionId;

}
