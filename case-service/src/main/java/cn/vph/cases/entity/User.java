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
 * @description 用户实体类
 * @create 2024/3/17 0:48
 */
@Data
@TableName("user")
public class User implements Serializable {

    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    private String nickname;

    private String type;

    private String password;

    @TableField("avatar_url")
    private String avatarUrl;

    private String email;

    private Integer level;

    /**
     * 用户学习过的病例
     */
    @TableField(exist = false)
    private List<Medcase> medcases;
}
