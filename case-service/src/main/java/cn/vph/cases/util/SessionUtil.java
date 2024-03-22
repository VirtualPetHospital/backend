package cn.vph.cases.util;


import cn.vph.cases.entity.User;
import cn.vph.common.CommonConstant;
import cn.vph.common.SessionData;
import cn.vph.common.util.BaseSessionUtil;
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
public class SessionUtil extends BaseSessionUtil {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private RedisUtil redisUtil;

    public SessionData getSessionData() {
        String key = request.getHeader(CommonConstant.SESSION);
        if (key == null) return null;

        SessionData sessionData = null;
        try {
            sessionData = (cn.vph.common.SessionData) redisUtil.get(key);
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
        String key = request.getHeader(CommonConstant.SESSION);
        redisUtil.del(key);
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

