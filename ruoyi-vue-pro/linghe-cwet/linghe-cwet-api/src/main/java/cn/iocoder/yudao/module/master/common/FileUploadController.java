package cn.iocoder.yudao.module.master.common;

import cn.iocoder.yudao.module.common.response.GlobalResponse;
import cn.iocoder.yudao.module.master.file.entity.FileMap;
import cn.iocoder.yudao.module.master.file.service.FileMapService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/pet/file")
@Validated
public class FileUploadController {

    @Resource
    private FileMapService fileService;

    // 上传文件
    @PostMapping("/upload")
    public GlobalResponse<FileMap> upload(@NotNull MultipartFile file) {
        return GlobalResponse.success(fileService.saveLocalFile(file));
    }


}
