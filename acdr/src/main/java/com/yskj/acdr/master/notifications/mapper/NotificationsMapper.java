package com.yskj.acdr.master.notifications.mapper;

import com.yskj.acdr.master.notifications.entity.Notifications;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 林河
 * @since 2024-08-12
 */
@Mapper
public interface NotificationsMapper extends BaseMapper<Notifications> {


    @Select("""
        select t.*
        from acdr_notifications t
        left join acdr_notifications_user u on t.id = u.notifications_id and u.user_id = #{userId}
        where t.status = 0 and u.notifications_id is null;
    """)
    List<Notifications> unread(long userId);



}
