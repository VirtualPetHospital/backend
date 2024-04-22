package cn.vph.files.util;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-04-22 13:52
 **/
public class TimeUtil {
    /**
     * 以s获取当前时间
     */
    public static long currentTime() {
        return System.currentTimeMillis() / 1000;
    }
}
