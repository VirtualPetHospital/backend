package cn.vph.cases.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Caroline
 * @description 疾病实体类
 * @create 2024/3/17 0:19
 */
@Data
@TableName("disease")
public class Disease implements Serializable{

    @TableId(value = "disease_id", type = IdType.AUTO)
    private Integer diseaseId;

    private String name;

    private String description;

    @TableField("category_id")
    private Integer categoryId;

    private String photo;

    private String video;
}
