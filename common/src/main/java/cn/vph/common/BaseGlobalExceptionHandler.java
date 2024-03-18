package cn.vph.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: vph-backend
 * @description: 统一异常处理
 * @author: astarforbae
 * @create: 2024-03-05 22:53
 **/
@Slf4j
@RestControllerAdvice
public class BaseGlobalExceptionHandler {

//    @ExceptionHandler(value = Exception.class)
//    public Result defaultErrorHandler(HttpServletRequest request, Exception e) {
//
//        Map<String, String> res = new HashMap<>();
//        Enumeration<?> temp = request.getParameterNames();
//        if (null != temp) {
//            while (temp.hasMoreElements()) {
//                String en = (String) temp.nextElement();
//                String value = request.getParameter(en);
//                res.put(en, value);
//                //如果字段的值为空，判断若值为空，则删除这个字段>
//                if (null == res.get(en) || "".equals(res.get(en))) {
//                    res.remove(en);
//                }
//            }
//        }
//        log.error("args: " + res);
//        log.error("e:" + e.getMessage());
//
//        StringWriter sw = new StringWriter();
//        PrintWriter pw = new PrintWriter(sw, true);
//        e.printStackTrace(pw);
//        pw.flush();
//        sw.flush();
//        log.error("exception: " + sw);
//
//
//        if (e instanceof CommonException) {
//            CommonException ce = (CommonException) e;
//            log.error("commonException: " + ce.getCommonErrorCode().getErrorReason() + "\n" + sw);
//            return Result.result(ce.getCommonErrorCode());
//        }
//        if (e instanceof MethodArgumentNotValidException) {
//            MethodArgumentNotValidException me = (MethodArgumentNotValidException) e;
//            log.error("MethodArgumentNotValidException: " + me.getMessage() + "\n" + sw);
//            return Result.result(CommonErrorCode.INVALID_PARAM, me.get());
//        }
//
//        return Result.result(CommonErrorCode.SYSTEM_ERROR);
//    }

    /**
     * 公共异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = CommonException.class)
    public Result commonErrorHandler(CommonException e) {
        log.error("CommonException: ", e);
        return Result.result(e.getCommonErrorCode());
    }

    /**
     * 参数校验异常
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException: ", e);
        List<String> info = e.getBindingResult().getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return Result.result(CommonErrorCode.INVALID_PARAM, info.toString());
    }

    @ExceptionHandler(value = Exception.class)
    public Result exceptionHandler(Exception e) {
        log.error("Exception: ", e);
        return Result.result(CommonErrorCode.SYSTEM_ERROR);
    }
}