package cn.vph.cases.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Caroline
 * @description 病种实体类
 * @create 2024/3/17 0:16
 */
@Data
@AllArgsConstructor
@TableName("category")
public class Category implements Serializable {

    @TableId(value = "category_id", type = IdType.AUTO)
    private Integer categoryId;

    private String name;
}
