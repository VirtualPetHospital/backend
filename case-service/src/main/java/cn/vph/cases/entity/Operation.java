package cn.vph.cases.entity;

import cn.vph.common.validation.VphValidation;
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

    @VphValidation("simpleName")
    private String name;

    @VphValidation("price")
    private Double price;

    @VphValidation("text")
    private String description;

    @VphValidation("fileName")
    private String photo;

    @VphValidation("fileName")
    private String video;
}
