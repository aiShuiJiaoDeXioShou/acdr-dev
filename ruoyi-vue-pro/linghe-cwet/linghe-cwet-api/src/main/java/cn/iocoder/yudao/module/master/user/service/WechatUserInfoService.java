package cn.iocoder.yudao.module.master.user.service;

import cn.iocoder.yudao.module.common.response.GlobalResponse;
import cn.iocoder.yudao.module.master.user.entity.Users;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author hjp
 * @since 2024-07-22
 */
public interface WechatUserInfoService extends IService<Users> {

    GlobalResponse<Users> wxLogin(String code);

    GlobalResponse<Users> update(MultipartFile file, Users wechatUserInfo);
}
