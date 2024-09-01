package cn.iocoder.yudao.module.common.verify;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = PhoneValidator.class)
@Target({ FIELD })
@Retention(RUNTIME)
public @interface ValidPhone {
    String message() default "无效的电话号码";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

