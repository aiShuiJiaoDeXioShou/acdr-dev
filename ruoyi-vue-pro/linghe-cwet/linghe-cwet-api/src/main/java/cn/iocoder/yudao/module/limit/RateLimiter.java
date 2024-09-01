package cn.iocoder.yudao.module.limit;

import java.lang.annotation.*;

/**
 * 限流
 *
 * @author hjp
 * @since 2024-07-10
 */
@Target(ElementType.METHOD) // 注解作用在方法上
@Retention(RetentionPolicy.RUNTIME) // 表示注解在运行时仍然可用，因此可以通过反射来读取。这对于运行时处理注解非常关键，比如切面编程（AOP）
@Documented // 当使用javadoc生成文档时，@RateLimiter注解也会出现在生成的文档中。
public @interface RateLimiter {
    /**
     * 限流key
     */
    String prefix() default "";

    /**
     * 限流时间,单位秒
     */
    int time() default 60;

    /**
     * 限流次数
     */
    int count() default 1;

    /**
     * 用于限流的参数名，如果为空且limitType为PARAMETER，则抛出异常
     */
    String paramName() default "";

    /**
     * 限流类型
     */
    LimitType limitType() default LimitType.IP;
}
