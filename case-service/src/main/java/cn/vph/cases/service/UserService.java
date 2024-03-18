package cn.vph.cases.service;

import cn.vph.cases.controller.request.RegisterRequest;
import cn.vph.cases.entity.User;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-17 18:23
 **/
public interface UserService {
    User login(String nickname, String password);

    User me();

    User register(RegisterRequest registerRequests);

    Object delete(Integer userId);

    User update(User user);

    Object sendCaptcha(String email);
}
