package cn.vph.common.util;

import cn.vph.common.CommonErrorCode;
import cn.vph.common.CommonException;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-17 20:05
 **/
public class AssertUtil {

    public AssertUtil() {
    }


    public static void isTrue(boolean expValue, CommonErrorCode resultCode) {
        if (!expValue) {
            throw new CommonException(resultCode);
        }
    }


    public static void isFalse(boolean expValue, CommonErrorCode resultCode) {
        isTrue(!expValue, resultCode);
    }

    public static void isEqual(Object obj1, Object obj2, CommonErrorCode resultCode) {
        isTrue(Objects.equals(obj1, obj2), resultCode);
    }

    public static void notEquals(Object obj1, Object obj2, CommonErrorCode resultCode) {
        isTrue(!Objects.equals(obj1, obj2), resultCode);
    }


    public static void contains(Object base, Collection<?> collection, CommonErrorCode resultCode) {
        notEmpty(collection, resultCode);
        isTrue(collection.contains(base), resultCode);
    }


    public static void blank(String str, CommonErrorCode resultCode) {
        isTrue(isBlank(str), resultCode);
    }

    public static void notBlank(String str, CommonErrorCode resultCode) {
        isTrue(!isBlank(str), resultCode);
    }

    public static void isNull(Object object, CommonErrorCode resultCode) {
        isTrue(object == null, resultCode);
    }

    public static void isNotNull(Object object, CommonErrorCode resultCode) {
        isTrue(object != null, resultCode);
    }


    public static void notEmpty(Collection collection, CommonErrorCode resultCode) {
        isTrue(!CollectionUtils.isEmpty(collection), resultCode);
    }

    public static void empty(Collection collection, CommonErrorCode resultCode) {
        isTrue(CollectionUtils.isEmpty(collection), resultCode);
    }

    public static void notEmpty(Map map, CommonErrorCode resultCode) {
        isTrue(!CollectionUtils.isEmpty(map), resultCode);
    }

    public static void empty(Map map, CommonErrorCode resultCode) {
        isTrue(CollectionUtils.isEmpty(map), resultCode);
    }

    private static boolean isBlank(String str) {
        int strLen;
        if (str != null && (strLen = str.length()) != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    public static void in(Object base, Object[] collection, CommonErrorCode resultCode) {
        isNotNull(collection, resultCode);
        boolean hasEqual = false;

        for (Object obj2 : collection) {
            if (base.equals(obj2)) {
                hasEqual = true;
                break;
            }
        }

        isTrue(hasEqual, resultCode);
    }

    public static void notIn(Object base, Object[] collection, CommonErrorCode resultCode) {
        if (null != collection) {

            for (Object obj2 : collection) {
//                isTrue(base != obj2, resultCode);
                isTrue(!base.equals(obj2), resultCode);
            }

        }
    }
}
