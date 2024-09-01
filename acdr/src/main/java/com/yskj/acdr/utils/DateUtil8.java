package com.yskj.acdr.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.io.File.separator;

/**
 * 日期工具
 *
 * @author hjp
 * @since 2024-07-10
 */
public class DateUtil8 {
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final DateTimeFormatter ISO_YMD_HMS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    /**
     * yyyy/MM/dd
     */
    public static final DateTimeFormatter URL_YMD = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    /**
     * 获取格式化后的当前日期时间（"yyyy-MM-dd HH:mm:ss"）
     */
    public static String getIsoNowDateTime() {
        return LocalDateTime.now().format(ISO_YMD_HMS);
    }

    /**
     * 获取指定日期的路径（yyyy/MM/dd）
     *
     * @param localDate 指定日期
     * @return 路径
     */
    public static String getPathByDate(LocalDate localDate) {
        return localDate.format(URL_YMD).replace("/", separator);
    }
}
