package cn.vph.cases.entity;

import cn.vph.cases.controller.request.UserRegisterRequest;
import cn.vph.cases.controller.request.UserUpdateRequest;
import cn.vph.common.validation.VphValidation;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @VphValidation("nickname")
    private String nickname;

    @VphValidation("userType")
    private String type;

    @VphValidation("password")
    private String password;

    @TableField("avatar_url")
    @VphValidation("fileName")
    private String avatarUrl;

    @VphValidation("email")
    private String email;

    private Integer level;

    @TableField("upgrade_progress")
    private Integer upgradeProgress;

    /**
     * 用户学习过的病例
     */
    @TableField(exist = false)
    private List<Medcase> medcases;

    public User(UserRegisterRequest userRegisterRequest){
        this.nickname = userRegisterRequest.getNickname();
        this.type = userRegisterRequest.getType();
        this.password = userRegisterRequest.getPassword();
        this.email = userRegisterRequest.getEmail();
    }
    public User(UserUpdateRequest user){
        this.userId = user.getUserId();
        this.nickname = user.getNickname();
        this.type = user.getType();
        this.password = user.getPassword();
        this.avatarUrl = user.getAvatarUrl();
        this.email = user.getEmail();
    }
}
