package cn.vph.cases.common;

import lombok.Getter;

/**
 * @program: vph-backend
 * @description: 统一异常处理类，例如：throw new CommonException(CommonErrorCode.USER_NOT_LOGGED_IN);
 * @author: astarforbae
 * @create: 2024-03-05 22:45
 **/
@Getter
public class CommonException extends RuntimeException {

    private final CommonErrorCode commonErrorCode;


    public CommonException(CommonErrorCode commonErrorCode) {
        this.commonErrorCode = commonErrorCode;
    }

}
