package com.yskj.acdr.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class FileUtils {
    public static String uploadSingleFile(MultipartFile file, String path, String flag) {
        String dirPath = path + flag + File.separator;
        FileUtil.mkdir(dirPath);
        String realName = file.getOriginalFilename();
        String fileName = dirPath + realName;
        File localFile = FileUtil.touch(fileName);
        try {
            file.transferTo(localFile);
        } catch (IOException e) {
            FileUtil.del(dirPath);
            throw new RuntimeException(e);
        }
        fileName = formatUri(fileName, path);
        return fileName;
    }

    /**
     * 格式化地址
     *
     * @param str 原字符串
     * @param del 删除字符串
     * @return 标准地址
     */
    public static String formatUri(String str, String del) {
        return str.replace(del, StrUtil.EMPTY).replace(StrPool.BACKSLASH, StrPool.SLASH);
    }

}
