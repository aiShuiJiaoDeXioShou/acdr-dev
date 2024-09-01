package com.yskj.acdr.limit;

import com.yskj.acdr.common.exception.LimitException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

/**
 * 限流切面类
 *
 * @author hjp
 * @since 2024-07-10
 */
@Slf4j
@Aspect
@Component
public class RateLimitAspect {

    private final StringRedisTemplate redisTemplate;

    public RateLimitAspect(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Pointcut("@annotation(RateLimiter)")
    public void rateLimitPointcut() {
    }

    /**
     * @param joinPoint 当前方法调用的上下文，可以获得-->方法签名（如方法名称、参数类型等）,方法注解,方法参数,目标对象（即被代理的对象）,代理对象
     * @return
     * @throws Throwable
     */
    @Around("rateLimitPointcut()")
    public Object rateLimit(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RateLimiter rateLimiter = method.getAnnotation(RateLimiter.class);

        String keyPrefix = rateLimiter.prefix();
        int time = rateLimiter.time();
        int count = rateLimiter.count();
        LimitType limitType = rateLimiter.limitType();
        String paramName = rateLimiter.paramName();

        String key = null;
        if (limitType == LimitType.IP) {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = null;
            if (attributes != null) {
                request = attributes.getRequest();
            }
            if (request != null) {
                key = keyPrefix + request.getRemoteAddr();
            }
        } else if (limitType == LimitType.PARAMETER) {
            Object[] args = joinPoint.getArgs();
            int paramIndex = getParamIndex(method, paramName);
            if (paramIndex < 0) {
                throw new IllegalArgumentException("名为:" + paramName + "的参数没有找到");
            }
            key = keyPrefix + args[paramIndex].toString();
        } else {
            throw new UnsupportedOperationException("不支持的限流类型: " + limitType);
        }
        // 调用Lua脚本执行限流逻辑
        limitRequestByLua(key, time, count);
        return joinPoint.proceed();
    }

    private int getParamIndex(Method method, String paramName) {
        Class<?>[] paramTypes = method.getParameterTypes();
        for (int i = 0; i < paramTypes.length; i++) {
            if (method.getParameters()[i].getName().equals(paramName)) {
                return i;
            }
        }
        return -1;
    }

    private void limitRequestByLua(String redisKey, int time, int count) {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        // Lua脚本进行限流
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("rateLimiter.lua")));
        redisScript.setResultType(Long.class);
        List<String> keys = Collections.singletonList(redisKey);
        // 执行Lua脚本
        Long result = redisTemplate.execute(redisScript, keys, String.valueOf(count), String.valueOf(time));
        if (result != null && result == 0L) {
            throw new LimitException("服务忙，请稍后重试");
        }
    }
}
