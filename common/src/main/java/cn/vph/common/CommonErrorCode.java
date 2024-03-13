package cn.vph.common;

import lombok.Getter;

/**
 * @program: vph-backend
 * @description: 封装错误信息，在发生异常时使用，例如：throw new CommonException(CommonErrorCode.USER_NOT_LOGGED_IN);
 * @author: astarforbae
 * @create: 2024-03-05 22:41
 **/
@Getter
public enum CommonErrorCode {

    /**
     * 相似的异常应该以相同的数字开头，例如：
     * 以1开头的和用户权限相关
     */
    USER_NOT_LOGGED_IN(1001, "用户未登录", "用户未登录"),
    SYSTEM_ERROR(-1, "系统异常，请查看日志", "系统异常，请稍后再试"),
    ;

    /**
     * 错误码
     */
    private final Integer errorCode;

    /**
     * 错误原因（给开发看的）
     */
    private final String errorReason;

    /**
     * 错误行动指示（给用户看的）
     */
    private final String errorSuggestion;

    CommonErrorCode(Integer errorCode, String errorReason, String errorSuggestion) {
        this.errorCode = errorCode;
        this.errorReason = errorReason;
        this.errorSuggestion = errorSuggestion;
    }

    @Override
    public String toString() {
        return "CommonErrorCode{" +
                "errorCode=" + errorCode +
                ", errorReason='" + errorReason + '\'' +
                ", errorSuggestion='" + errorSuggestion + '\'' +
                '}';
    }
}