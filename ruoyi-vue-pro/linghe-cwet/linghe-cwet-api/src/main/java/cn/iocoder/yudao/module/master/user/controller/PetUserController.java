package cn.iocoder.yudao.module.master.user.controller;

import cn.iocoder.yudao.module.utils.LoginUtil;
import cn.iocoder.yudao.module.common.response.GlobalResponse;
import cn.iocoder.yudao.module.master.user.entity.Users;
import cn.iocoder.yudao.module.master.user.service.UsersService;
import cn.iocoder.yudao.module.master.user.service.WechatUserInfoService;
import cn.iocoder.yudao.module.utils.CommonUtil;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class PetUserController {

    @Resource
    private UsersService usersService;

    @Resource
    private WechatUserInfoService wechatUserInfoService;

    /**
     * 获取验证码
     *
     * @param phone 客户电话号
     * @return 验证码
     */
    //@RateLimiter(prefix = "limit:phone:", limitType = LimitType.PARAMETER, paramName = "phone")
    @GetMapping("/pet/public/getCode")
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
    @PostMapping("/pet/public/login")
    public GlobalResponse<String> login(@NotBlank(message = "电话号码不能为空") String phone, @NotBlank(message = "电话号码不能为空") String code) {
        if (!CommonUtil.isValidPhoneNumber(phone)) {
            return GlobalResponse.failure("电话号码格式错误");
        }
        return usersService.login(phone, code);
    }

    /**
     * @author linghe
     * 登录之后获取用户信息
     */
    @GetMapping("/pet/user/userinfo")
    public GlobalResponse<Users> userInfo() {
        Users userInfo = usersService.getById(LoginUtil.getLoginIdAsLong());
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
    @GetMapping("/pet/user/logout")
    public GlobalResponse<String> logout() {
        LoginUtil.logout();
        return GlobalResponse.success("退出登录成功！");
    }

    @GetMapping("/pet/public/wxLogin")
    public GlobalResponse<Users> wxLogin(@NotBlank(message = "请输入code码") String code) {
        return wechatUserInfoService.wxLogin(code);
    }

}
