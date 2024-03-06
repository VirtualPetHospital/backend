package com.ecnu.vphbackend.aspect;

import com.ecnu.vphbackend.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-05 22:55
 **/
@Aspect
@Component
@Slf4j
public class ResultAspect {
    @Around("execution(public * com.ecnu.vphbackend.controller.*.*(..))")
    public Object doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
            Object object = proceedingJoinPoint.proceed();
            if(Objects.isNull(object) || !object.getClass().equals(Result.class)){
                object = Result.success(object);
            }
            return object;
    }
}

