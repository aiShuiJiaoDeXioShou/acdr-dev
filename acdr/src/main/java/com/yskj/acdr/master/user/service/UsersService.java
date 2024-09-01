package com.yskj.acdr.master.user.service;

import com.yskj.acdr.common.response.GlobalResponse;
import com.yskj.acdr.master.user.entity.Users;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 林河
 * @since 2024-08-08
 */
public interface UsersService extends IService<Users> {

    /**
     * 获取验证码
     *
     * @param phone 客户电话号
     * @return 验证码
     */
    GlobalResponse<String> getCode(String phone);

    GlobalResponse<String> getMhCode(String phone);

    /**
     * 使用手机号进行登录操作
     *
     * @param phone 客户电话号
     * @param code  发送给客户的验证码
     * @return 登录信息
     */
    GlobalResponse<String> login(String phone, String code);

    Users getSafeUsers(Long userId);

    Users getSafeUsers();

    String loginShop(String phone);
}
