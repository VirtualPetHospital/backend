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
    ILLEGAL_USER_INFO(1009, "请求字段中有非法值或非法修改某个字段", "请求参数异常"),
    /**
     * 疾病相关
     */
    DISEASE_NOT_EXIST(11001, "疾病不存在", "疾病不存在"),
    DISEASE_ALREADY_EXIST(11002, "疾病已存在", "疾病已存在"),
    CANNOT_DELETE_DISEASE(11003, "该疾病下有病例，无法删除", "该疾病下有病例，无法删除"),
    /**
     * 手术相关
     */
    OPERATION_NOT_EXIST(12001, "手术不存在", "手术不存在"),
    OPERATION_ALREADY_EXIST(12002, "手术已存在", "手术已存在"),
    /**
     * 病种相关
     */
    CANNOT_DELETE_CATEGORY(13001, "该病种下有疾病，无法删除", "该病种下有疾病，无法删除"),
    CATEGORY_NOT_EXIST(13002, "病种不存在", "病种不存在"),
    CATEGORY_ALREADY_EXIST(13003, "病种已存在", "病种已存在"),
    /**
     * 药品相关
     */
    MEDICINE_NOT_EXIST(14002, "药品不存在", "药品不存在"),
    MEDICINE_ALREADY_EXIST(13003, "药品已存在", "药品已存在"),
    /**
     * 检查项目相关
     */
    INSPECTION_NOT_EXIST(15001, "检查项目不存在", "检查项目不存在"),
    INSPECTION_ALREADY_EXIST(15002, "检查项目已存在", "检查项目已存在"),
    /**
     * 科室相关
     */
    ROOM_NOT_EXIST(16001, "科室不存在", "科室不存在"),
    ROOM_ALREADY_EXIST(16002, "科室已存在", "科室已存在"),
    /**
     * 科室设施相关
     */
    ROOM_ASSET_NOT_EXIST(17001, "科室设施不存在", "科室设施不存在"),
    ROOM_ASSET_ALREADY_EXIST(17002, "科室设施已存在", "科室设施已存在"),
    CANNOT_CHANGE_ROOM_OF_ROOM_ASSET(17003, "科室设施所属科室不可修改", "科室设施所属科室不可修改"),

    /**
     * 文件相关
     */
    FILE_NOT_EXIST(18001, "文件不存在", "文件不存在"),
    READ_FILE_ERROR(18002, "读取文件失败", "读取文件失败"),
    FILE_ALREADY_EXIST(18003, "文件已存在", "文件已存在"),
    FILE_UPLOAD_FAIL(18004, "文件上传失败，请重试", "文件上传失败，请重试"),
    FILE_DOWNLOAD_FAIL(18005, "文件下载失败，请重试", "文件下载失败，请重试"),
    FILE_LOCATION_ERROR(18006, "文件存储位置非法", "文件存储位置非法"),
    FILE_TYPE_ERROR(18007, "文件类型非法", "文件类型非法"),

    /**
     * 以2开头的和业务相关
     * <p>
     * 不存在: 2002
     * 不合法参数: 2003
     */
    RELATIONSHIP_STILL_EXIST(2001, "删除失败，被其他表引用", "删除失败，被其他表引用"),


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
     * 考试相关
     */
    EXAM_NOT_EXIST(2002, "考试不存在", "考试不存在"),
    EXAM_NAME_ALREADY_EXIST(2003, "考试名称已存在", "考试名称已存在"),
    EXAM_TIME_INVALID(2003, "考试时间设置不合法", "考试时间设置不合法"),
    EXAM_DURATION_INVALID(2003, "考试时长不合法", "考试时长不合法"),
    EXAM_ALREADY_ENROLLED(2003, "已报名该考试", "已报名该考试"),
    /**
     * 答题卡相关
     */
    NOT_ENROLLED(2002, "未报名该考试", "未报名该考试"),
    ANSWER_SHEET_NOT_EXIST(2002, "答题卡不存在", "答题卡不存在"),
    ANSWERS_NOT_EXIST(2003, "答案不能为空", "答案不能为空"),
    ANSWER_SHEET_ALREADY_EXIST(2003, "已提交过答题卡", "已提交过答题卡"),
    /**
     * 病例相关
     */

    MEDCASE_NOT_EXIST(2002, "病例不存在", "病例不存在"),

    /**
     * 智能对话助手相关
     */
    GPT_CONNECTION_ERROR(3001, "连接失败或超时", "系统繁忙，请稍后再尝试"),

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