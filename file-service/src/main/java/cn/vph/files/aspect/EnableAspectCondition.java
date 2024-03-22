package cn.vph.files.aspect;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-21 12:02
 **/
public class EnableAspectCondition implements Condition {

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        String enableAspect = conditionContext.getEnvironment().getProperty("vph.aspect.enable");
        return false;
    }
}
