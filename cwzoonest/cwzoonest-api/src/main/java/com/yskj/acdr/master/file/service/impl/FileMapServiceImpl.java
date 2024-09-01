package com.yskj.acdr.master.file.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.yskj.acdr.master.file.entity.FileMap;
import com.yskj.acdr.master.file.mapper.FileMapMapper;
import com.yskj.acdr.master.file.service.FileMapService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yskj.acdr.utils.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * <p>
 *  服务实现类
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
     * @param file 删除的文件
     * @return 上传结果
     */
    @Override
    public FileMap saveLocalFile(MultipartFile file) {
        String flag = StpUtil.getLoginIdAsLong() + File.separator + "upload";
        String fileName = FileUtils.uploadSingleFile(file, filePath, flag);
        // 创建 FileMap 对象并保存到数据库
        FileMap fileMap = new FileMap()
                .setUrl("/profile/"+fileName)
                .setIsExist(true)
                .setIsLocal(true)
                .setIsWeb(false)
                .setLocalFilePath(fileName);
        // 将文件信息保存到数据库
        this.save(fileMap);
        return fileMap;
    }

}
