package cn.iocoder.yudao.module.master.user.service.impl;

import cn.iocoder.yudao.module.utils.LoginUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.iocoder.yudao.module.common.constant.GlobalConstant;
import cn.iocoder.yudao.module.common.exception.LoginFailedException;
import cn.iocoder.yudao.module.common.response.GlobalResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.iocoder.yudao.module.master.common.WeChatProperties;
import cn.iocoder.yudao.module.master.user.entity.Users;
import cn.iocoder.yudao.module.master.user.mapper.UsersMapper;
import cn.iocoder.yudao.module.master.user.service.WechatUserInfoService;
import cn.iocoder.yudao.module.utils.FileUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hjp
 * @since 2024-07-22
 */
@Service
public class WechatUserInfoServiceImpl extends ServiceImpl<UsersMapper, Users>
        implements WechatUserInfoService {
    @Value("${path.profile}")
    private String profile;
    @Value("${sky.wechat.login}")
    private String wxLoginUrl;
    @Resource
    private WeChatProperties weChatProperties;
    @Resource
    private UsersMapper wechatUserInfoMapper;

    /**
     * 微信用户登录
     *
     * @param code code码
     * @return
     */
    @Override
    public GlobalResponse<Users> wxLogin(String code) {
        //调用微信服务器获取openid
        String openid = getOpenid(code);
        //判断openid是否为空
        if (StrUtil.isBlank(openid)) {
            throw new LoginFailedException("登录失败");
        }
        //判断是否是新用户
        Users one = wechatUserInfoMapper.selectOne(new LambdaQueryWrapper<Users>().eq(Users::getOpenid, openid));
        if (one == null) {
            //新用户，自动注册
            Users wechatUserInfo = new Users();
            wechatUserInfo.setName("宠物达人").setOpenid(openid).setCreateTime(LocalDateTime.now());
            wechatUserInfoMapper.insert(wechatUserInfo);
            one = wechatUserInfoMapper.selectOne(new LambdaQueryWrapper<Users>().eq(Users::getOpenid, openid));
        }
        //登录
        LoginUtil.login(one.getId());
        String token = LoginUtil.getTokenValue();
        return GlobalResponse.success(token, one);
    }

    @Override
    public GlobalResponse<Users> update(MultipartFile file, Users wechatUserInfo) {
        Users one = wechatUserInfoMapper.selectById(wechatUserInfo.getId());
        if (one == null) {
            return GlobalResponse.failure("用户不存在");
        }
        if (file != null) {
            if (StrUtil.isNotBlank(one.getAvatar())) {
                //删除旧头像
                FileUtil.del(profile + one.getAvatar());
            }
            String fileName = FileUtils.uploadSingleFile(file, profile, "avatar" + File.separator + one.getId());
            wechatUserInfo.setAvatar(fileName);
        }
        //用户名唯一
        if (StrUtil.isNotBlank(wechatUserInfo.getName())) {
            LambdaQueryWrapper<Users> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Users::getName, wechatUserInfo.getName()).ne(Users::getId, one.getId());
            if (wechatUserInfoMapper.selectCount(wrapper) > 0) {
                return GlobalResponse.failure("用户名已存在，请更换后重试！");
            }
        }
        //手机号唯一
        if (StrUtil.isNotBlank(wechatUserInfo.getPhone())) {
            LambdaQueryWrapper<Users> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Users::getPhone, wechatUserInfo.getPhone()).ne(Users::getId, one.getId());
            if (wechatUserInfoMapper.selectCount(wrapper) > 0) {
                return GlobalResponse.failure("手机号已存在，请更换后重试！");
            }
        }
        //身份证号唯一
        if (StrUtil.isNotBlank(wechatUserInfo.getTypeId())) {
            LambdaQueryWrapper<Users> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Users::getTypeId, wechatUserInfo.getTypeId()).ne(Users::getId, one.getId());
            if (wechatUserInfoMapper.selectCount(wrapper) > 0) {
                return GlobalResponse.failure("身份证号已存在，请更换后重试！");
            }
        }
        int i = wechatUserInfoMapper.updateById(wechatUserInfo);
        if (i > 0) {
            return GlobalResponse.success(GlobalConstant.UPDATE + GlobalConstant.SUCCESS, wechatUserInfo);
        } else {
            return GlobalResponse.failure(GlobalConstant.UPDATE + GlobalConstant.FAILURE);
        }
    }

    /**
     * 获取openid
     *
     * @param code code码
     */
    private String getOpenid(String code) {
        //调用微信服务器获取openid
        Map<String, Object> map = new HashMap<>();
        map.put("appid", weChatProperties.getAppid());
        map.put("secret", weChatProperties.getSecret());
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");
        String json = HttpUtil.get(wxLoginUrl, map);
        JSONObject jsonObject = JSONUtil.parseObj(json);
        return jsonObject.getStr("openid");
    }
}




