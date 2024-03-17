package cn.vph.exam.common;

import cn.vph.common.BaseGlobalExceptionHandler;
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
public class GlobalExceptionHandler extends BaseGlobalExceptionHandler {
}
