package cn.vph.cases.controller.request;

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

    private String nickname;

    private String type;

    private String password;

    private String email;

    private String captcha;

    private String avatarUrl;
}
