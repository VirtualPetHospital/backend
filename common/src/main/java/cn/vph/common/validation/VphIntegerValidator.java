package cn.vph.common.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-29 22:51
 **/
public class VphIntegerValidator implements ConstraintValidator<VphValidation, Integer> {
    private String rule;

    @Override
    public void initialize(VphValidation constraintAnnotation) {
        this.rule = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        if("posInt".equals(rule)) {
            // 不为空，不为null，正整数
            return integer != null && integer > 0;
        } else if ("userLevel".equals(rule)) {
            // 不为空，不为null，1-5
            return integer != null && integer >= 1 && integer <= 5;
        } else if("duration".equals(rule)){
            // 不为空，不为null，1-300
            return integer != null && integer >= 1 && integer <= 300;
        } else if("questionNum".equals(rule)){
            // 不为空，不为null，int，范围1-40
            return integer != null && integer >= 1 && integer <= 40;
        }else if ("level".equals(rule)) {
            // 不为空，不为null，1-5
            return integer != null && integer >= 1 && integer <= 5;
        }
        else {
            return false;
        }
    }


}
