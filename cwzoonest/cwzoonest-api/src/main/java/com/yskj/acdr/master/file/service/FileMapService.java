package com.yskj.acdr.master.file.service;

import com.yskj.acdr.master.file.entity.FileMap;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 林河
 * @since 2024-08-14
 */
public interface FileMapService extends IService<FileMap> {


    FileMap saveLocalFile(MultipartFile file);
}
