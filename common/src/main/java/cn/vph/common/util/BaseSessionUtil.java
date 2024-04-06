package cn.vph.common.util;

import cn.vph.common.CommonConstant;
import cn.vph.common.SessionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;


@Component
public class BaseSessionUtil {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private BaseRedisUtil redisUtil;


    public Integer getUserId() {
        return Optional
                .ofNullable(getSessionData())
                .orElse(new SessionData())
                .getUserId();
    }

    public String getUserType() {
        return Optional
                .ofNullable(getSessionData())
                .orElse(new SessionData())
                .getType();
    }

    public Integer getUserLevel() {
        return Optional
                .ofNullable(getSessionData())
                .orElse(new SessionData())
                .getLevel();
    }

    public SessionData getSessionData() {
        String key = request.getHeader(CommonConstant.SESSION);
        if (key == null) return null;

        SessionData sessionData;
        try {
            sessionData = (SessionData) redisUtil.get(key);
        } catch (Exception e) {
            return null;
        }
        return sessionData;
    }

    public String getSessionId(){
        return request.getHeader(CommonConstant.SESSION);
    }
}
