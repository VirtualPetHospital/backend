package cn.vph.cases.controller.request;

import cn.vph.common.validation.VphValidation;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-04-21 09:52
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    @VphValidation("nickname")
    private String nickname;

    @VphValidation("userType")
    private String type;

    @VphValidation("passwordNullable")
    private String password;

    @TableField("avatar_url")
    @VphValidation("fileName")
    private String avatarUrl;

    @VphValidation("email")
    private String email;
}
