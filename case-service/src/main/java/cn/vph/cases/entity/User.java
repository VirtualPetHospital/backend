package cn.vph.cases.entity;

import cn.vph.cases.controller.request.RegisterRequest;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.List;

/**
 * @author Caroline
 * @description 用户实体类
 * @create 2024/3/17 0:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
public class User implements Serializable {

    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    private String nickname;

    private String type;

    private String password;

    @TableField("avatar_url")
    private String avatarUrl;

    @Email
    private String email;

    private Integer level;

    /**
     * 用户学习过的病例
     */
    @TableField(exist = false)
    private List<Medcase> medcases;

    public User(RegisterRequest registerRequest){
        this.nickname = registerRequest.getNickname();
        this.type = registerRequest.getType();
        this.password = registerRequest.getPassword();
        this.email = registerRequest.getEmail();
    }
}
