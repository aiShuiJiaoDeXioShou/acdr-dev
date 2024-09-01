package com.yskj.acdr.master.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yskj.acdr.common.response.GlobalResponse;
import com.yskj.acdr.master.user.entity.Users;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author hjp
 * @since 2024-07-22
 */
public interface WechatUserInfoService extends IService<Users> {

    GlobalResponse<Users> wxLogin(String code);

    GlobalResponse<Users> update(MultipartFile file, Users wechatUserInfo);
}
