package com.yskj.acdr.utils;

/**
 * 通用工具类
 *
 * @author hjp
 * @since 2024-07-10
 */
public class CommonUtil {
    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() != 11) {
            return false;
        }
        String regex = "^1[3-9]\\d{9}$";

        return phoneNumber.matches(regex);
    }
}
