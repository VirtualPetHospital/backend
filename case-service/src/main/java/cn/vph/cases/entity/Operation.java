package cn.vph.cases.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Caroline
 * @description 手术实体类
 * @create 2024/3/17 0:44
 */
@Data
@TableName("operation")
public class Operation implements Serializable {

    @TableId(value = "operation_id", type = IdType.AUTO)
    private Integer operationId;

    private String name;

    private Double price;

    private String description;

    private String photo;

    private String video;
}
