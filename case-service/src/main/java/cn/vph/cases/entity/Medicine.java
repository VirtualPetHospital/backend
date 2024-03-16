package cn.vph.cases.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Caroline
 * @description 药品实体类
 * @create 2024/3/17 0:38
 */
@Data
@TableName("medicine")
public class Medicine implements Serializable {

    @TableId(value = "medicine_id", type = IdType.AUTO)
    private Integer medicineId;

    private String name;

    private Double price;
}
