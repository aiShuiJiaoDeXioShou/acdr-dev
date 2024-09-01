package com.yskj.acdr.master.file.service;

import com.yskj.acdr.master.file.entity.FileMap;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 林河
 * @since 2024-08-14
 */
public interface FileMapService extends IService<FileMap> {

    /**
     * 保存到本地服务器上面
     * @param file 删除的文件
     * @return 上传结果
     */
    FileMap saveLocalFile(MultipartFile file);

    /**
     * 保存 BufferedImage 到本地服务器上
     * @param image BufferedImage 对象
     * @param formatName 图片格式（如 "png", "jpg"）
     * @return 上传结果
     */
    FileMap saveLocalFile(BufferedImage image, String formatName);

    FileMap saveLocalFile(BufferedImage image, String imageName, String formatName);

    String getLocalFilePath(FileMap fileMap);
}
