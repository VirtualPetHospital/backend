package cn.vph.common.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-29 22:51
 **/
public class VphValidator implements ConstraintValidator<VphValidation, String> {
    private String rule;

    @Override
    public void initialize(VphValidation constraintAnnotation) {
        this.rule = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        if ("name".equals(rule)) {
            // 不为空，不为null，长度小于100，由汉字、大小写字母、数字、下划线（_）、连字符（-）构成
            return !isNull(value) && !isEmpty(value) && value.matches("[\\u4E00-\\u9FA5a-zA-Z0-9_-]+") && value.length() < 100;
        } else if ("simpleName".equals(rule)) {
            // 不为空，不为null，长度小于100，由汉字、大小写字母、数字组成
            return !isNull(value) && !isEmpty(value) && value.matches("[\\u4E00-\\u9FA5a-zA-Z0-9]+") && value.length() < 100;
        } else if ("text".equals(rule)) {
            // 不为空，不为null，最长5000个字符
            return !isNull(value) && !isEmpty(value) && value.length() < 5000;
        } else if ("fileName".equals(rule)) {
            // 不为空，可以null，长度小于100，由汉字、大小写字母、数字、下划线（_）、连字符（-）构成
            return isNull(value) || (!isEmpty(value) && value.matches("[\\u4E00-\\u9FA5a-zA-Z0-9_-]+") && value.length() < 100);
        } else if ("num".equals(rule)) {
            // 不为空，不为null，两位小数，默认为0.00，范围：0.00-99999.99，
            return !isNull(value) && !isEmpty(value) && value.matches("^(0|[1-9][0-9]{0,4})(\\.[0-9]{1,2})?$");
        } else if ("time".equals(rule)) {
            //  不为空，不为null，时间，格式：yyyy-MM-dd HH:mm:ss
            return !isNull(value) && !isEmpty(value) && value.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$");
        } else {
            return false;
        }
    }

    public boolean isNull(String value) {
        return value == null;
    }

    public boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }
}
