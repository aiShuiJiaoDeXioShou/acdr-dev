package com.yskj.acdr.master.user.controller;

import com.yskj.acdr.common.response.GlobalResponse;
import com.yskj.acdr.enums.AuthenticationErrorCode;
import com.yskj.acdr.master.user.service.AuthenticationService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Validated
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Resource
    private AuthenticationService service;

    /**
     * 身份认证接口
     * @param file 上传身份证正面
     * @return 返回认证结果
     */
    @PostMapping
    public GlobalResponse<AuthenticationErrorCode> AuthId(@RequestParam(value = "file", required = false) MultipartFile file) {
        AuthenticationErrorCode errorCode = service.AuthId(file);
        if (errorCode.equals(AuthenticationErrorCode.SUCCESS)) {
            return GlobalResponse.success(errorCode.getCode());
        }
        return GlobalResponse.failure(errorCode.getMessage(), errorCode);
    }

    /**
     * @author 林河
     * 判断当前用户是否实名认证
     */
    @GetMapping("/isRealName")
    public GlobalResponse<Boolean> isRealName() {
        return GlobalResponse.success(service.isRealName());
    }

}
