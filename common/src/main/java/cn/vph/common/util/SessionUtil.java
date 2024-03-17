//package cn.vph.common.util;
//
//import cn.vph.common.CommonConstant;
//import cn.vph.common.SessionData;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.Optional;
//import java.util.UUID;
//
//
//@Component
//public class SessionUtil {
//
//    @Autowired
//    private HttpServletRequest request;
//
//    @Autowired
//    private HttpServletResponse response;
//
//    @Autowired
//    private RedisUtil redisUtil;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    public Integer getUserId(){
//        return Optional
//                .ofNullable(getSessionData())
//                .orElse(new SessionData())
//                .getUserId();
//    }
//
////    public Integer getUserType(){
////        return Optional
////                .ofNullable(getSessionData())
////                .orElse(new SessionData())
////                .getType();
////    }
//
//    public SessionData getSessionData(){
//        String key = request.getHeader(CommonConstant.SESSION);
//        if(key == null) return null;
//
//        SessionData sessionData = null;
//        try {
//            sessionData = (SessionData) redisUtil.get(key);
//        }catch (Exception e){
//            return getSessionDataFromDB(key);
//
//        }
//        if(sessionData != null)return sessionData;
//        return getSessionDataFromDB(key);
//    }
//
//    public String getSessionId() {
//        String key = request.getHeader(CommonConstant.SESSION);
//        if(key == null) return null;
//        return  key;
//    }
//
//    public void setSessionId(String sessionId){
//        response.setHeader(CommonConstant.SESSION,sessionId);
//    }
//
//    public String generateSessionId(){
//        String sessionId = UUID.randomUUID().toString();
//        response.setHeader(CommonConstant.SESSION, sessionId);
//        return sessionId;
//    }
//
//    public void invalidate(){
//        request.removeAttribute(CommonConstant.SESSION);
//    }
//
//    private SessionData getSessionDataFromDB(String key) {
//        SessionData sessionData;
//        UserEntity userEntity = userRepository.findByEmail(key);
//        if(userEntity != null){
//            sessionData = new SessionData(userEntity);
//            redisUtil.set(key,sessionData);
//            return sessionData;
//        }else{
//            redisUtil.set(key,null,3600);
//            return null;
//        }
//    }
//}
