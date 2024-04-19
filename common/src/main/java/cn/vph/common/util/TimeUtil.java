package cn.vph.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-04-19 10:24
 **/
public class TimeUtil {

    /**
     * 获取当前时间，并转换为yyyy-MM-dd HH:mm:ss格式，返回字符串
     */
    public static String getCurrentTime() {
        //获取当前时间 并转换为yyyy-MM-dd HH:mm:ss格式
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
