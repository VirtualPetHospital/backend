package cn.vph.cases.util;


import cn.vph.cases.entity.User;
import cn.vph.cases.mapper.UserMapper;
import cn.vph.common.CommonConstant;
import cn.vph.common.SessionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-17 16:51
 **/

@Component
public class SessionUtil extends cn.vph.common.util.BaseSessionUtil {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserMapper userMapper;

    public SessionData getSessionData() {
        String key = request.getHeader(CommonConstant.SESSION);
        if (key == null) return null;

        SessionData sessionData = null;
        try {
            sessionData = (SessionData) redisUtil.get(key);
        } catch (Exception e) {
            return null;

        }
        if (sessionData != null) return sessionData;
        return null;
    }

    /**
     * 用户登出，清除session
     */
    public void invalidate() {
        request.removeAttribute(CommonConstant.SESSION);
    }

    /**
     * 设置session
     */
    public void setSession(User user) {
        SessionData sessionData = new SessionData(user.getUserId(), user.getType(), user.getLevel());
        String key = UUID.randomUUID().toString();
        redisUtil.set(key, sessionData, 86400); // 24小时过期
        response.setHeader(CommonConstant.SESSION, key);
    }

}

