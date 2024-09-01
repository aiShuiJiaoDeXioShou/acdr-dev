package dev.cwzoonest.cn.framework.web.service;

import dev.cwzoonest.cn.common.core.domain.entity.SysRole;
import dev.cwzoonest.cn.system.service.ISysRoleService;
import dev.cwzoonest.cn.system.service.impl.SysUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import dev.cwzoonest.cn.common.constant.CacheConstants;
import dev.cwzoonest.cn.common.constant.Constants;
import dev.cwzoonest.cn.common.constant.UserConstants;
import dev.cwzoonest.cn.common.core.domain.entity.SysUser;
import dev.cwzoonest.cn.common.core.domain.model.RegisterBody;
import dev.cwzoonest.cn.common.core.redis.RedisCache;
import dev.cwzoonest.cn.common.exception.user.CaptchaException;
import dev.cwzoonest.cn.common.exception.user.CaptchaExpireException;
import dev.cwzoonest.cn.common.utils.MessageUtils;
import dev.cwzoonest.cn.common.utils.SecurityUtils;
import dev.cwzoonest.cn.common.utils.StringUtils;
import dev.cwzoonest.cn.framework.manager.AsyncManager;
import dev.cwzoonest.cn.framework.manager.factory.AsyncFactory;
import dev.cwzoonest.cn.system.service.ISysConfigService;
import dev.cwzoonest.cn.system.service.ISysUserService;

/**
 * 注册校验方法
 *
 */
@Component
public class SysRegisterService
{
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private SysUserServiceImpl sysUserServiceImpl;

    @Autowired
    private ISysRoleService roleService;

    /**
     * 注册
     */
    public String register(RegisterBody registerBody)
    {
        String msg = "", username = registerBody.getUsername(), password = registerBody.getPassword();
        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);

        // 验证码开关
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (captchaEnabled)
        {
            validateCaptcha(username, registerBody.getCode(), registerBody.getUuid());
        }

        if (StringUtils.isEmpty(username))
        {
            msg = "用户名不能为空";
        }
        else if (StringUtils.isEmpty(password))
        {
            msg = "用户密码不能为空";
        }
        else if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            msg = "账户长度必须在2到20个字符之间";
        }
        else if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            msg = "密码长度必须在5到20个字符之间";
        }
        else if (!userService.checkUserNameUnique(sysUser))
        {
            msg = "保存用户'" + username + "'失败，注册账号已存在";
        }
        else
        {
            sysUser.setNickName(username);
            sysUser.setPassword(SecurityUtils.encryptPassword(password));
            boolean regFlag = userService.registerUser(sysUser);
            if (!regFlag)
            {
                msg = "注册失败,请联系系统管理人员";
            }
            else
            {
                // 给注册的该用户赋予普通人员的权限
                SysUser user = sysUserServiceImpl.selectUserByUserName(username);
                if (user != null) {
                    SysRole sysRole = roleService.selectRoleById(2L);
                    if (sysRole.getRoleKey().equals("common")) {
                        roleService.insertAuthUsers(2L,new Long[]{user.getUserId()});
                    } else {
                        msg = "授予用户权限失败，没有普通用户权限(数据库用户权限ID为2)，请检查数据库是否把普通用户权限的位置改变了！";
                        return msg;
                    }
                }
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.REGISTER, MessageUtils.message("user.register.success")));
            }
        }
        return msg;
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public void validateCaptcha(String username, String code, String uuid)
    {
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null)
        {
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha))
        {
            throw new CaptchaException();
        }
    }
}
