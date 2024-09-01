package cn.iocoder.yudao.module.utils;

import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;

/**
 * 实现这里的登录方法，连接两端的功能
 */
public class LoginUtil {
    public static Long getLoginIdAsLong() {
        return SecurityFrameworkUtils.getLoginUserId();
    }

    public static void login(Long userId) {

    }

    public static String getTokenValue() {
        return "";
    }

    public static void logout() {

    }
}
