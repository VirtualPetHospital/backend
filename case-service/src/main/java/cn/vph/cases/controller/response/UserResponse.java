package cn.vph.cases.controller.response;

import cn.vph.cases.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-31 19:53
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Integer userId;
    private String nickname;
    private String type;
    private String avatarUrl;
    private String email;
    private Integer level;
    public UserResponse(User u){
        this.userId = u.getUserId();
        this.nickname = u.getNickname();
        this.type = u.getType();
        this.avatarUrl = u.getAvatarUrl();
        this.level = u.getLevel();
        this.email = u.getEmail();
    }
}
