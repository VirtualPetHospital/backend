package cn.vph.cases.service;

import cn.vph.cases.controller.request.UserRegisterRequest;
import cn.vph.cases.controller.request.UserUpdateRequest;
import cn.vph.cases.controller.response.UserResponse;
import cn.vph.cases.entity.User;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-17 18:23
 **/
public interface UserService extends IService<User> {
    UserResponse login(String nickname, String password);

    UserResponse me();

    UserResponse register(UserRegisterRequest userRegisterRequests);

    Object delete(Integer userId);

    UserResponse update(UserUpdateRequest user);

    Object sendCaptcha(String email);

    Object logout();

    void checkNickname(String nickname);

    void checkEmail(String email);

    IPage<?> list(Integer pageNum, Integer pageSize, String type, String nicknameKeyword, Integer sortByNickname);

    IPage<?> medcases(Integer pageNum, Integer pageSize, String medcaseInfoKeyword, String medcaseNameKeyword, String disease, Integer sortByViewTime);

    void upgrade(Integer numCurrentLevel, Integer userId, String sessionId);

    List<String> getNicknames(List<Integer> values);
}
