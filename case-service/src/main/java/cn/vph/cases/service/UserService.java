package cn.vph.cases.service;

import cn.vph.cases.controller.request.RegisterRequest;
import cn.vph.cases.entity.User;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-17 18:23
 **/
public interface UserService extends IService<User> {
    User login(String nickname, String password);

    User me();

    User register(RegisterRequest registerRequests);

    Object delete(Integer userId);

    User update(User user);

    Object sendCaptcha(String email);

    Object logout();

    void checkNickname(String nickname);

    void checkEmail(String email);

    IPage<?> list(Integer pageNum, Integer pageSize, String type, String nicknameKeyword, Integer sortByNickname);

    IPage<?> medcases(Integer pageNum, Integer pageSize, String medcaseInfoKeyword, String medcaseNameKeyword, String disease, Integer sortByViewTime);
}
