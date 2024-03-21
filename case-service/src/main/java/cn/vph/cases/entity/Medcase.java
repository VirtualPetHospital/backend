package cn.vph.cases.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Caroline
 * @description 病例实体类
 * @create 2024/3/17 0:23
 */
@Data
@TableName("medcase")
public class Medcase implements Serializable {

    @TableId(value = "medcase_id",  type = IdType.AUTO)
    private Integer medcaseId;

    private Double price;

    @TableField("diagnose_result")
    private String diagnoseResult;

    @TableField("disease_id")
    private Integer diseaseId;

    private String name;

    @TableField("info_description")
    private String infoDescription;

    @TableField("info_photo")
    private String infoPhoto;

    @TableField("info_video")
    private String infoVideo;

    @TableField("treatment_description")
    private String treatmentDescription;

    @TableField("operation_id")
    private Integer operationId;

    @TableField(exist = false)
    private Disease disease;

    @TableField(exist = false)
    private Operation operation;

    @TableField(exist = false)
    private List<MedcaseInspection> inspections;

    @TableField(exist = false)
    private List<MedcaseMedicine> medicines;
}
