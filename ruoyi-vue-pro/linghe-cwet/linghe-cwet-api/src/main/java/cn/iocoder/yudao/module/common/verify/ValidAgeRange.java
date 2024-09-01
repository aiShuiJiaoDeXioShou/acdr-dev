package cn.iocoder.yudao.module.common.verify;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AgeRangeValidator.class)
public @interface ValidAgeRange {
    String message() default "年龄必须在 {min} 到 {max} 岁之间";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    int min() default 0; // 最小年龄
    int max() default 150; // 最大年龄
}

