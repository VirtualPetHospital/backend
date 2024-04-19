package cn.vph.cases.entity;

import cn.vph.common.validation.VphValidation;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Caroline
 * @description 检查项实体类
 * @create 2024/3/17 0:21
 */
@Data
@TableName("inspection")
public class Inspection implements Serializable {

    @TableId(value = "inspection_id", type = IdType.AUTO)
    private Integer inspectionId;

    @VphValidation("inspectionName")
    private String name;

    @VphValidation("number")
    private Double low;

    @VphValidation("number")
    private Double high;

}
