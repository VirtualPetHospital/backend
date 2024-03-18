package cn.vph.cases.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-18 08:58
 **/
@Component
public class CaptchaUtil {
    private static final String SYMBOLS = "0123456789ABCDEFGHIGKLMNOPQRSTUVWXYZ";
    private static final Random RANDOM = new SecureRandom();

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 生成随机验证码
     * @return
     */
    private String generateCaptcha() {
        //	如果是六位，就生成大小为 6 的数组
        char[] numbers = new char[6];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        return new String(numbers);
    }

    /**
     * Redis 缓存Captcha
     * @param key
     * @return
     */
    public String setCaptcha(String key) {
        String captcha = generateCaptcha();
        redisUtil.set(key, captcha, 300); // 5分钟过期
        return captcha;
    }

    /**
     * 获取Redis缓存的Captcha
     * @param key
     * @return
     */
    public String getCaptcha(String key) {
        return (String) redisUtil.get(key);
    }
}
