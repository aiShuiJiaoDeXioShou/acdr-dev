package com.yskj.acdr.master.user.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.yskj.acdr.common.cache.GlobalRedisCache;
import com.yskj.acdr.common.response.GlobalResponse;
import com.yskj.acdr.master.user.entity.*;
import com.yskj.acdr.master.user.service.UsersService;
import com.yskj.acdr.master.user.service.WechatUserInfoService;
import com.yskj.acdr.utils.CommonUtil;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
@Validated
public class UserController {

    @Resource
    private UsersService usersService;

    @Resource
    private WechatUserInfoService wechatUserInfoService;

    @Autowired
    private GlobalRedisCache<String> redisCache;


    /**
     * 获取验证码
     *
     * @param phone 客户电话号
     * @return 验证码
     */
    //@RateLimiter(prefix = "limit:phone:", limitType = LimitType.PARAMETER, paramName = "phone")
    @GetMapping("/public/getCode")
    public GlobalResponse<String> getCode(@NotBlank(message = "电话号码不能为空") String phone) {
        if (!CommonUtil.isValidPhoneNumber(phone)) {
            return GlobalResponse.failure("电话号码格式错误");
        }
        return usersService.getMhCode(phone);
    }

    /**
     * 使用手机号进行登录操作
     *
     * @param phone 客户电话号
     * @param code  发送给客户的验证码
     * @return 登录信息
     */
    @PostMapping("/public/login")
    public GlobalResponse<Map<Object, Object>> login(@NotBlank(message = "电话号码不能为空") String phone, @NotBlank(message = "电话号码不能为空") String code) {
        if (!CommonUtil.isValidPhoneNumber(phone)) {
            return GlobalResponse.failure("电话号码格式错误");
        }
        GlobalResponse<String> login = usersService.login(phone, code);
        String userShop;
        UserShopInfo userShopInfo = null;
        if (StrUtil.isNotBlank(login.getData())) {
            // 登录商城账户
            userShop = usersService.loginShop(phone);
            userShopInfo = JSONUtil.parse(userShop).toBean(UserShopInfo.class);
        }
        return GlobalResponse.success(
                MapUtil.builder()
                        .put("userToken", login.getData())
                        .put("shopLoginUser", userShopInfo)
                        .map()
        );
    }

    /**
     * 获取用户商城登录信息
     * 返回获取的信息
     */
    @PostMapping("/shopLogin")
    public GlobalResponse<UserShopInfo> getShopLoginUser() {
        String data = redisCache.get(StpUtil.getLoginIdAsString() + "_shopLogin");
        if (StrUtil.isBlank(data)) {
            long id = StpUtil.getLoginIdAsLong();
            Users nowUser = usersService.getById(id);
            String shopUserData = usersService.loginShop(nowUser.getPhone());
            return StrUtil.isBlank(shopUserData) ?
                    GlobalResponse.failure() :
                    GlobalResponse.success(JSONUtil.parseObj(shopUserData).toBean(UserShopInfo.class));
        }
        return GlobalResponse.success(JSONUtil.parseObj(data).toBean(UserShopInfo.class));
    }

    /**
     * @author linghe
     * 登录之后获取用户信息
     */
    @GetMapping("/user/userinfo")
    public GlobalResponse<Users> userInfo() {
        Users userInfo = usersService.getById(StpUtil.getLoginIdAsLong());
        userInfo.setTypeId("")
                .setCreateTime(null)
                .setUpdateTime(null)
                .setEmail("");
        return GlobalResponse.success("获取用户信息成功！", userInfo);
    }

    /**
     * @author linghe
     * <p>
     * 退出登录
     */
    @GetMapping("/user/logout")
    public GlobalResponse<String> logout() {
        StpUtil.logout();
        return GlobalResponse.success("退出登录成功！");
    }

    @GetMapping("/public/wxLogin")
    public GlobalResponse<Users> wxLogin(@NotBlank(message = "请输入code码") String code) {
        return wechatUserInfoService.wxLogin(code);
    }

}
