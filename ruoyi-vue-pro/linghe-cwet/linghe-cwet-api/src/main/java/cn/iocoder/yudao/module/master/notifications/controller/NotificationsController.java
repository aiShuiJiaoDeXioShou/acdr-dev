package cn.iocoder.yudao.module.master.notifications.controller;

import cn.iocoder.yudao.module.utils.LoginUtil;
import cn.iocoder.yudao.module.master.notifications.entity.Notifications;
import cn.iocoder.yudao.module.master.notifications.entity.NotificationsUser;
import cn.iocoder.yudao.module.master.notifications.mapper.NotificationsMapper;
import cn.iocoder.yudao.module.master.notifications.mapper.NotificationsUserMapper;
import cn.iocoder.yudao.module.common.response.GlobalResponse;
import io.swagger.annotations.Api;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 林河
 * @since 2024-08-12
 */
@Api(tags = "消息通知系统")
@RestController
@RequestMapping("/pet/notifications")
@Validated
public class NotificationsController {

    @Resource
    private NotificationsMapper notificationsMapper;

    // 这是消息被阅读的类
    @Resource
    private NotificationsUserMapper notificationsUserMapper;

    /**
     * @author 林河
     * 获取该用户所有未读的消息
     */
    @GetMapping
    public GlobalResponse<List<Notifications>> unread() {
        return GlobalResponse.success(notificationsMapper.unread(LoginUtil.getLoginIdAsLong()));
    }

    /**
     * @author 林河
     * 这是消息被阅读的方法
     * 消息点击被阅读
     */
    @PostMapping
    public GlobalResponse<Integer> read(@NotNull(message = "消息ID不能为空") Long messageId) {
        return GlobalResponse.success(notificationsUserMapper.insert(
                new NotificationsUser()
                        .setNotifications(messageId)
                        .setUserId(LoginUtil.getLoginIdAsLong())
        ));
    }
}
