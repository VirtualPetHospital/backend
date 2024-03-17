package cn.vph.exam.aspect;

import cn.vph.common.CommonConstant;
import cn.vph.common.CommonErrorCode;
import cn.vph.common.SessionData;
import cn.vph.common.util.AssertUtil;
import cn.vph.exam.util.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-17 20:45
 **/
@Aspect
@Component
@Slf4j
public class TeacherAspect {

    @Autowired
    private SessionUtil sessionUtil;

    @Around("@annotation(cn.vph.common.annotation.Teacher)")
    public Object doAroundTeacher(ProceedingJoinPoint joinPoint) throws Throwable {
        SessionData sessionData = sessionUtil.getSessionData();

        AssertUtil.isNotNull(sessionData, CommonErrorCode.USER_NOT_LOGGED_IN);

        AssertUtil.in(sessionData.getType(), CommonConstant.AT_LEAST_TEACHER,  CommonErrorCode.UNAUTHORIZED_ACCESS);

        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

        //log
        log.error("------------");
        log.error("operator: " + sessionData.getUserId());
        log.error("operation: " + method.getName());

        return joinPoint.proceed();
    }
}
