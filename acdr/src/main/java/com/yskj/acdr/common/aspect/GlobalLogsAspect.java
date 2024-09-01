package com.yskj.acdr.common.aspect;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 接口访问日志输出
 *
 * @author hjp
 * @since 2024-07-10
 */
@Slf4j
@Aspect
@Component
public class GlobalLogsAspect {

    private static final String LOCAL_IPV4 = "127.0.0.1";
    private static final String LOCAL_IPV6 = "0:0:0:0:0:0:0:1";

    @Resource
    private HttpServletRequest httpServletRequest;

    @Pointcut(value = "execution(public com.yskj.acdr.common.response.GlobalResponse com.yskj.acdr.master.*..controller.*.*(..))")
    public void pointcut() {
    }

    @Around(value = "pointcut()")
    public Object log(ProceedingJoinPoint pjp) throws Throwable {
        long s = System.currentTimeMillis();
        Object proceed;
        StringBuilder logInfo = new StringBuilder();
        try {
            String ip = getClientIp();
            logInfo.append("请求端地址：").append(ip).append("；");
            String type = httpServletRequest.getMethod();
            logInfo.append("请求类型：").append(type).append("；");
            String path = httpServletRequest.getServletPath();
            logInfo.append("请求方法：").append(path).append("；");

            String args;
            String contentType = httpServletRequest.getContentType();
            if ("application/json".equals(contentType)) {
                args = "......";
            } else {
                MethodSignature signature = Convert.convert(MethodSignature.class, pjp.getSignature());
                String[] pns = signature.getParameterNames();
                Object[] pvs = pjp.getArgs();
                Dict entries = Dict.create();
                int pl = pns.length;
                for (int pi = 0; pi < pl; pi++) {
                    String pn = pns[pi];
                    Object pv = pvs[pi];
                    if (!StrUtil.containsAnyIgnoreCase(pn, "pwd", "password")) {
                        if (pv instanceof IPage || pv instanceof MultipartFile) {
                            // 不记录分页信息和文件对象
                            pv = pv.getClass().getSimpleName() + StrUtil.AT + pv.hashCode();
                        }
                    } else {
                        pv = "********";
                    }
                    entries.set(pn, pv);
                }
                args = JSONUtil.toJsonStr(entries);
            }
            logInfo.append("请求参数：").append(args).append("；");

            // 以上为前置通知
            proceed = pjp.proceed();
            // 以下为后置通知

            //logInfo.append("响应数据：").append(proceed).append("；");

            long e = System.currentTimeMillis();
            logInfo.append("响应耗时：").append(e - s).append("毫秒");
            log.info(logInfo.toString());
        } catch (Throwable throwable) {
            // 此处为异常通知
            long e = System.currentTimeMillis();
            logInfo.append("响应耗时：").append(e - s).append("毫秒");
            log.error(logInfo.toString());
            // 打印完日志后原样抛出，交给全局异常处理
            throw throwable;
        }
        return proceed;
    }

    /**
     * 获取客户端IP
     *
     * @return 客户端IP
     */
    public String getClientIp() {
        // 客户端IP地址
        String clientIp = LOCAL_IPV4;
        // 获取转发IP列表
        String xForwardFor = httpServletRequest.getHeader("X-Forwarded-For");
        if (xForwardFor != null) {
            // 转发IP列表不为空，循环列表找到真实IP
            String[] ips = xForwardFor.split(",");
            for (String ip : ips) {
                if (!LOCAL_IPV4.equals(ip) && !LOCAL_IPV6.equals(ip)) {
                    clientIp = ip;
                    break;
                }
            }
        } else {
            // 转发IP列表为空，尝试获取Nginx记录的真实IP
            String xRealIp = httpServletRequest.getHeader("X-Real-IP");
            if (xRealIp != null) {
                // Nginx记录的真实IP不为空
                if (LOCAL_IPV6.equals(xRealIp)) {
                    clientIp = xRealIp;
                }
            } else {
                /*
                    Apache: Proxy-Client-IP
                    WebLogic: WL-Proxy-Client-IP
                */
                String remoteAddr = httpServletRequest.getRemoteAddr();
                if (LOCAL_IPV6.equals(remoteAddr)) {
                    clientIp = remoteAddr;
                }
            }
        }
        return clientIp;
    }

}