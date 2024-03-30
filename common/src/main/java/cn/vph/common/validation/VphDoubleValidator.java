package cn.vph.common.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-29 22:51
 **/
public class VphDoubleValidator implements ConstraintValidator<VphValidation, Double> {
    private String rule;

    @Override
    public void initialize(VphValidation constraintAnnotation) {
        this.rule = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Double aDouble, ConstraintValidatorContext constraintValidatorContext) {
        if ("price".equals(rule) || "number".equals(rule)) {
            // 不为空，不为null，两位小数，默认为0.00，范围：0.00-99999.99，
            return aDouble != null && aDouble >= 0.00 && aDouble <= 99999.99;
        } else {
            return false;
        }
    }

}
