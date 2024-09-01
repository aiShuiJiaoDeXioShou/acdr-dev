package com.yskj.acdr.master.notifications.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yskj.acdr.common.response.GlobalResponse;
import com.yskj.acdr.master.notifications.entity.Notifications;
import com.yskj.acdr.master.notifications.entity.NotificationsUser;
import com.yskj.acdr.master.notifications.mapper.NotificationsMapper;
import com.yskj.acdr.master.notifications.mapper.NotificationsUserMapper;
import com.yskj.acdr.master.notifications.service.NotificationsService;
import io.swagger.annotations.Api;
import jakarta.annotation.Resource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 林河
 * @since 2024-08-12
 */
@Api(tags = "消息通知系统")
@RestController
@RequestMapping("/notifications")
@Validated
public class NotificationsController {

    @Resource
    private NotificationsMapper notificationsMapper;

    @Resource
    private NotificationsService notificationsService;

    // 这是消息被阅读的类
    @Resource
    private NotificationsUserMapper notificationsUserMapper;

    /**
     * @author 林河
     * 获取该用户所有未读的消息
     */
    @GetMapping
    public GlobalResponse<List<Notifications>> unread() {
        return GlobalResponse.success(notificationsMapper.unread(StpUtil.getLoginIdAsLong()));
    }

    /**
     * @author 林河
     * 获取用户想要显示的所有消息
     */
    @GetMapping("/list")
    public GlobalResponse<List<Notifications>> list() {
        return GlobalResponse.success(notificationsService.list());
    }

    /**
     * @author 林河
     * 这是消息被阅读的方法
     * 消息点击被阅读
     */
    @PostMapping("/readList")
    @Transactional
    public GlobalResponse<String> reads(@RequestBody List<Long> messageIdList) {
        long userId = StpUtil.getLoginIdAsLong();
        for (Long l : messageIdList) {
            Long count = notificationsUserMapper.selectCount(new LambdaQueryWrapper<NotificationsUser>()
                    .eq(NotificationsUser::getUserId, userId)
                    .eq(NotificationsUser::getNotifications, l));
            if (count == null) {
                notificationsUserMapper.insert(new NotificationsUser()
                        .setNotifications(l)
                        .setUserId(userId));
            }

        }
        return GlobalResponse.success("消息阅读成功！");
    }

    /**
     * @author 林河
     * 这是消息被阅读的方法
     * 消息点击被阅读
     */
    @PostMapping
    public GlobalResponse<Integer> read(@RequestParam Long messageId) {
        return GlobalResponse.success(notificationsUserMapper.insert(
                new NotificationsUser()
                        .setNotifications(messageId)
                        .setUserId(StpUtil.getLoginIdAsLong())
        ));
    }
}
