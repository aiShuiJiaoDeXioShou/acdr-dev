package cn.iocoder.yudao.module.master.file.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.iocoder.yudao.module.common.exception.BusinessException;
import cn.iocoder.yudao.module.master.file.entity.FileMap;
import cn.iocoder.yudao.module.master.file.mapper.FileMapMapper;
import cn.iocoder.yudao.module.master.file.service.FileMapService;
import cn.iocoder.yudao.module.utils.LoginUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.iocoder.yudao.module.utils.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 林河
 * @since 2024-08-14
 */
@Service
public class FileMapServiceImpl extends ServiceImpl<FileMapMapper, FileMap> implements FileMapService {

    // 从配置文件中获取自定义路径
    @Value("${path.file}")
    private String filePath;

    /**
     * 保存到本地服务器上面
     *
     * @param file 删除的文件
     * @return 上传结果
     */
    @Override
    public FileMap saveLocalFile(MultipartFile file) {
        String flag = LoginUtil.getLoginIdAsLong() + "/" + "upload";
        String fileName = FileUtils.uploadSingleFile(file, filePath, flag);
        // 创建 FileMap 对象并保存到数据库
        FileMap fileMap = new FileMap()
                .setUrl("/profile/" + fileName)
                .setIsExist(true)
                .setIsLocal(true)
                .setIsWeb(false)
                .setLocalFilePath(fileName);
        // 将文件信息保存到数据库
        this.save(fileMap);
        return fileMap;
    }


    /**
     * 保存 BufferedImage 到本地服务器上
     *
     * @param image      BufferedImage 对象
     * @param formatName 图片格式（如 "png", "jpg"）
     * @return 上传结果
     */
    @Override
    public FileMap saveLocalFile(BufferedImage image, String formatName) {
        return saveLocalFile(image, IdUtil.getSnowflakeNextIdStr(), formatName);
    }


    /**
     * 保存 BufferedImage 到本地服务器上
     *
     * @param image      BufferedImage 对象
     * @param formatName 图片格式（如 "png", "jpg"）
     * @return 上传结果
     */
    @Override
    public FileMap saveLocalFile(BufferedImage image, String imageName, String formatName) {
        // 生成文件名
        String flag = LoginUtil.getLoginIdAsLong() + "/" + "generate";
        String uniqueFileName = imageName + "." + formatName;
        String path =  flag + "/" + uniqueFileName;
        String fullPath = filePath + "/" + path;

        // 创建目标目录
        File directory = new File(filePath + "/" + flag);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 保存 BufferedImage 为文件
        try {
            File outputFile = new File(fullPath);
            ImageIO.write(image, formatName, outputFile);
        } catch (IOException e) {
            throw new RuntimeException("保存图片失败", e);
        }

        // 创建 FileMap 对象并保存到数据库
        FileMap fileMap = new FileMap()
                .setUrl("/profile/" + path)
                .setIsExist(true)
                .setIsLocal(true)
                .setIsWeb(false)
                .setLocalFilePath(path);

        // 将文件信息保存到数据库
        this.save(fileMap);

        return fileMap;
    }

    /**
     * 获取本地路径的文件
     */
    @Override
    public String getLocalFilePath(FileMap fileMap) {
        if (!fileMap.getIsLocal()) {
            throw new BusinessException("这不是本地文件,请从云服务器里面获取！");
        }
        return filePath + fileMap.getLocalFilePath();
    }

}
