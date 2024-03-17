package cn.vph.cases.aspect;

import cn.vph.cases.util.SessionUtil;
import cn.vph.common.CommonConstant;
import cn.vph.common.CommonErrorCode;
import cn.vph.common.SessionData;
import cn.vph.common.util.AssertUtil;
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
public class AdministratorAspect {

    @Autowired
    private SessionUtil sessionUtil;

    @Around("@annotation(cn.vph.common.annotation.Administrator)")
    public Object doAroundAdministrator(ProceedingJoinPoint joinPoint) throws Throwable {
        SessionData sessionData = sessionUtil.getSessionData();

        AssertUtil.isNotNull(sessionData, CommonErrorCode.USER_NOT_LOGGED_IN);

        AssertUtil.in(sessionData.getType(), CommonConstant.AT_LEAST_ADMINISTRATOR, CommonErrorCode.UNAUTHORIZED_ACCESS);

        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

        //log
        log.error("------------");
        log.error("operator: " + sessionData.getUserId());
        log.error("operation: " + method.getName());

        return joinPoint.proceed();
    }
}
