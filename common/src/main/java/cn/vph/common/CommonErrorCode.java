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
    INVALID_PARAM(-1, "请求参数未通过校验，具体请查看日志", "请求参数异常"),
    USER_NOT_LOGGED_IN(1001, "用户未登录", "用户未登录"),
    UNAUTHORIZED_ACCESS(1002, "用户试图进行越权操作", "无权限！"),
    NOT_CURRENT_USER(1003, "用户试图修改其他用户的信息", "仅可对当前用户进行操作"),
    USER_NOT_EXIST(1004, "用户不存在", "用户不存在"),
    WRONG_PASSWORD_OR_NICKNAME(1005, "用户名或密码错误", "用户名或密码错误"),
    NICKNAME_ALREADY_EXIST(1006, "用户名已存在", "用户名已存在"),
    EMAIL_ALREADY_EXIST(1007, "邮箱已注册", "邮箱已注册"),
    WRONG_CAPTCHA(1008, "验证码错误", "验证码错误"),


    /**
     * 以2开头的和业务相关
     *
     * 不存在: 2002
     * 不合法参数: 2003
     */
    RELATIONSHIP_STILL_EXIST(2001, "删除失败，被其他表引用", "删除失败，被其他表引用"),

    ILLEGAL_USER_INFO(2003, "请求字段中有非法值或非法修改某个字段", "请求参数异常"),


    /**
     * 题目相关
     */
    QUESTION_NOT_EXIST(2002, "题目不存在", "题目不存在"),
    QUESTION_ANSWER_NOT_VALID(2003, "answer字段不合法，题目答案只能是A/B/C/D", "answer字段不合法，题目答案只能是A/B/C/D"),
    /**
     * 试卷相关
     */
    PAPER_NOT_EXIST(2002, "试卷不存在", "试卷不存在"),
    QUESTION_NUM_INVALID(2003, "题目数量不合法", "题目数量不合法"),
    PAPER_NAME_ALREADY_EXIST(2003, "试卷名称已存在", "试卷名称已存在"),
    /**
     * 病例相关
     */
    DISEASE_NOT_EXIST(2002, "疾病不存在", "疾病不存在"),
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