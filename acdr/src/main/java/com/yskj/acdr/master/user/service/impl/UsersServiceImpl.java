package com.yskj.acdr.master.user.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yskj.acdr.common.cache.GlobalRedisCache;
import com.yskj.acdr.common.response.GlobalResponse;
import com.yskj.acdr.common.verify.PhoneValidator;
import com.yskj.acdr.master.user.entity.Users;
import com.yskj.acdr.master.user.mapper.UsersMapper;
import com.yskj.acdr.master.user.service.UsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yskj.acdr.utils.MutualHttpUtil;
import com.yskj.acdr.utils.ProfileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static cn.hutool.crypto.SecureUtil.md5;
import static com.yskj.acdr.common.constant.GlobalConstant.FAILURE;
import static com.yskj.acdr.common.constant.GlobalConstant.SUCCESS;
import org.springframework.beans.factory.annotation.Value;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 林河
 * @since 2024-08-08
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {

    private static final String DEFAULT_NICK_NAME_PREFIX = "宠物达人";

    @Value("${path.profile}")
    private String profile;
    @Value("${phone.key}")
    private String key;
    @Value("${phone.model}")
    private String model;
    @Value("${phone.url}")
    private String url;
    @Value("${profiles:active}")
    private String debug;

    @Autowired
    private GlobalRedisCache<String> redisCache;

    @Override
    public GlobalResponse<String> getCode(String phone) {
        String code = ArrayUtil.join(NumberUtil.generateRandomNumber(0, 10, 6), "");
        code = "#code#=" + code;
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", phone);
        params.put("tpl_id", model);
        params.put("tpl_value", code);
        params.put("key", key);
        String result = HttpUtil.get("http://api.smsbao.com/", params);
        JSONObject object = JSONUtil.parseObj(result);
        if (object.getInt("error_code") == 0 || ProfileUtil.isDebug()) {
            log.debug("验证码：" + code);
            //将code入缓存
            redisCache.set(phone, code.substring(7), 5, TimeUnit.MINUTES);
            return GlobalResponse.success("验证码获取" + SUCCESS, code.substring(7));
        } else {
            log.debug("验证码获取失败-" + object.get("error_code") + ":" + object.get("reason"));
            return GlobalResponse.failure("验证码获取" + FAILURE);
        }
    }

    @Override
    public GlobalResponse<String> getMhCode(String phone) {
        // 获取配置数据
        String smsapi = "http://api.smsbao.com/";
        String user = "549555305"; // 短信平台帐号
        String pass = md5("95ce429593b04bd5a657d38d7499c140"); // 短信平台密码
        String sign = "【宠屋，通知您】"; // 短信签名
        // 生成随机验证码
        String code = ArrayUtil.join(NumberUtil.generateRandomNumber(0, 10, 6), "");
        // 设置短信内容
        String content = sign + "您的短信验证码是：" + code;
        // 发送短信请求
        String sendurl = smsapi + "sms?u=" + user + "&p=" + pass + "&m=" + phone + "&c=" + URLEncoder.encode(content, StandardCharsets.UTF_8);
        String result = HttpUtil.get(sendurl);
        // 解析返回结果
        if ("0".equals(result) || ProfileUtil.isDebug()) { // 发送成功
            log.debug("验证码：" + code);
            // 将验证码存入缓存
            redisCache.set(phone, code, 5, TimeUnit.MINUTES);
            return GlobalResponse.success("验证码获取成功", code);
        } else if (result == null) {
            log.debug("短信接口请求失败");
            return GlobalResponse.failure("短信接口请求失败");
        } else {
            log.debug("验证码获取失败 - " + result);
            return GlobalResponse.failure("验证码获取失败");
        }
    }


    @Override
    public GlobalResponse<String> login(String phone, String code) {
        // 验证验证码
        String cachedCode = redisCache.get(phone);
        if (cachedCode == null || !cachedCode.equals(code)) {
            return GlobalResponse.failure("验证码错误或已过期");
        }

        // 检查手机号是否已注册
        Users user = this.baseMapper.selectOne(new QueryWrapper<Users>().eq("phone", phone));
        if (user == null) {
            // 未注册，创建新用户
            user = new Users()
                    .setAvatar("/profile/avatar.jpg")
                    .setName(phone)  // 设置默认用户名
                    .setPhone(phone)
                    .setCreateTime(LocalDateTime.now())
                    .setUpdateTime(LocalDateTime.now())
                    .setNickname(DEFAULT_NICK_NAME_PREFIX + phone.substring(phone.length() - 4));
            this.baseMapper.insert(user);
        }

        // 使用 SaToken 进行登录
        StpUtil.login(user.getId());

        // 返回成功响应
        return GlobalResponse.success("登录成功", StpUtil.getTokenValue());
    }

    /**
     * 在安全情况下获取用户的数据
     * @param userId 用户id
     * @return 处理过后的用户数据
     */
    @Override
    public Users getSafeUsers(Long userId) {
        Users users = baseMapper.selectById(userId);
        return users.setOpenid("")
                .setCreateTime(null)
                .setUpdateTime(null)
                .setEmail("")
                .setTypeId("")
                // 这个是创建用户的手机号信息隐藏中间几位
                .setPhone(PhoneValidator.hidePhoneNumber(users.getPhone()));
    }

    /**
     * 在安全情况下获取当前用户的数据
     * @return 处理过后的用户数据
     */
    @Override
    public Users getSafeUsers() {
        Users users = baseMapper.selectById(StpUtil.getLoginIdAsLong());
        return users.setOpenid("")
                .setCreateTime(null)
                .setUpdateTime(null)
                .setEmail("")
                .setTypeId("")
                // 这个是创建用户的手机号信息隐藏中间几位
                .setPhone(PhoneValidator.hidePhoneNumber(users.getPhone()));
    }

    @Override
    public String loginShop(String phone) {
        // 登录商城账户
        String res = MutualHttpUtil.sendPostRequest(
                "http://127.0.0.1:48080/app-api/member/auth/registered",
                Map.of("mobile", phone,
                        "password", phone),"1");
        JSONObject entries = JSONUtil.parseObj(res);
        String userData = null;
        if (entries.get("code", Integer.class) == 0) {
            userData = entries.get("data", String.class);
            Long expiresTime = JSONUtil.parseObj(userData).get("expiresTime", Long.class);
            // 保存商城用户登录信息到缓存当中
            redisCache.set(StpUtil.getLoginIdAsString()+"_shopLogin", userData, expiresTime, TimeUnit.MINUTES);
            return userData;
        }
        return userData;
    }

}
