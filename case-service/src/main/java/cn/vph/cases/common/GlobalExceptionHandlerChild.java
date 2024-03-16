package cn.vph.cases.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @program: vph-backend
 * @description: 集成全局异常处理器
 * @author: astarforbae
 * @create: 2024-03-16 13:54
 **/
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandlerChild extends cn.vph.common.GlobalExceptionHandler{
}
