package cn.vph.cases.controller.request;

import cn.vph.common.validation.VphValidation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-18 09:01
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @VphValidation("nickname")
    private String nickname;

    @VphValidation("userType")
    private String type;

    @VphValidation("password")
    private String password;

    @VphValidation("email")
    private String email;

    private String captcha;

    @VphValidation("fileName")
    private String avatarUrl;
}
