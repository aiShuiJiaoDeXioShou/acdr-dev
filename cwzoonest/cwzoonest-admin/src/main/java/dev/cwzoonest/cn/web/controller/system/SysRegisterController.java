package dev.cwzoonest.cn.web.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import dev.cwzoonest.cn.common.core.controller.BaseController;
import dev.cwzoonest.cn.common.core.domain.AjaxResult;
import dev.cwzoonest.cn.common.core.domain.model.RegisterBody;
import dev.cwzoonest.cn.common.utils.StringUtils;
import dev.cwzoonest.cn.framework.web.service.SysRegisterService;
import dev.cwzoonest.cn.system.service.ISysConfigService;

/**
 * 注册验证
 * 
 */
@RestController
public class SysRegisterController extends BaseController
{
    @Autowired
    private SysRegisterService registerService;

    @Autowired
    private ISysConfigService configService;

    @PostMapping("/register")
    public AjaxResult register(@RequestBody RegisterBody user)
    {
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser"))))
        {
            return error("当前系统没有开启注册功能！");
        }
        String msg = registerService.register(user);
        return StringUtils.isEmpty(msg) ? success() : error(msg);
    }
}
