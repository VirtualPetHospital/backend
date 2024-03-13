package cn.vph.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: vph-backend
 * @description: 统一返回实体
 * @author: astarforbae
 * @create: 2024-03-05 22:38
 **/
@Data
public class Result<T> implements Serializable {

    private Integer code;
    private String msg;
    private T data;

    /**
     * 无返回数据的成功返回
     *
     * @param <T>
     * @return
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * @param data 返回数据
     */
    public static <T> Result<T> success(T data) {
        return success("操作成功", data);
    }

    /**
     * @param mess 返回信息
     * @param data 返回数据
     */
    public static <T> Result<T> success(String mess, T data) {
        Result<T> m = new Result<>();
        m.setCode(0);
        m.setData(data);
        m.setMsg(mess);
        return m;
    }

    /**
     * 出现异常时的返回
     * 不用手动调，抛异常时异常处理器会自动处理
     *
     * @param commonErrorCode 错误码
     * @param data            返回数据
     */
    public static <T> Result<T> result(CommonErrorCode commonErrorCode, T data) {
        Result<T> m = new Result<>();
        m.setCode(commonErrorCode.getErrorCode());
        m.setData(data);
        m.setMsg(commonErrorCode.getErrorSuggestion());
        return m;
    }

    public static <T> Result<T> result(CommonErrorCode commonErrorCode) {
        return result(commonErrorCode, null);
    }
}