<<<<<<< HEAD
<<<<<<< HEAD:exam-service/src/main/java/cn/vph/exam/common/CommonErrorCode.java
package cn.vph.exam.common;

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
     * 以1开头的和用户操作相关
     */
    SYSTEM_ERROR(-1, "系统异常，请查看日志", "系统异常，请稍后再试"),

    USER_NOT_LOGGED_IN(1001, "用户未登录", "用户未登录"),
    UNAUTHORIZED_ACCESS(1002, "用户试图进行越权操作", "无权访问"),
    NOT_CURRENT_USER(1003, "用户试图修改其他用户的信息", "仅可对当前用户进行操作"),
    USER_NOT_EXIST(1004, "用户不存在", "用户不存在"),
    WRONG_PASSWORD_OR_NICKNAME(1005, "用户名或密码错误", "用户名或密码错误"),
    NICKNAME_ALREADY_EXIST(1006, "用户名已存在", "用户名已存在"),
    EMAIL_ALREADY_EXIST(1007, "邮箱已注册", "邮箱已注册"),
    WRONG_CAPTCHA(1008, "验证码错误", "验证码错误"),
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
=======
=======
>>>>>>> qinDev
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
    SYSTEM_ERROR(-1, "系统异常，请查看日志", "系统异常，请稍后再试"),

    USER_NOT_LOGGED_IN(1001, "用户未登录", "用户未登录"),
    UNAUTHORIZED_ACCESS(1002, "用户试图进行越权操作", "无权访问"),
    NOT_CURRENT_USER(1003, "用户试图修改其他用户的信息", "仅可对当前用户进行操作"),
    USER_NOT_EXIST(1004, "用户不存在", "用户不存在"),
    WRONG_PASSWORD_OR_NICKNAME(1005, "用户名或密码错误", "用户名或密码错误"),
    NICKNAME_ALREADY_EXIST(1006, "用户名已存在", "用户名已存在"),
    EMAIL_ALREADY_EXIST(1007, "邮箱已注册", "邮箱已注册"),
    WRONG_CAPTCHA(1008, "验证码错误", "验证码错误"),
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
<<<<<<< HEAD
>>>>>>> qinDev:common/src/main/java/cn/vph/common/CommonErrorCode.java
=======
>>>>>>> qinDev
}