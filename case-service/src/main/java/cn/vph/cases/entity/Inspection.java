package cn.vph.cases.entity;

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

    private String name;

    private Double low;

    private Double high;

}
