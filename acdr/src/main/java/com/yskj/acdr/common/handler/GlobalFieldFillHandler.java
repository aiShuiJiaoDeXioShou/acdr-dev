package com.yskj.acdr.common.handler;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.yskj.acdr.utils.DateUtil8;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectionException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 通过MP框架自动填充“插入时间”与“更新时间”
 *
 * @author hjp
 * @since 2024-07-10
 */
@Component
@Slf4j
public class GlobalFieldFillHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());

        // 仅在用户已登录且 metaObject 中存在但未设置 userId 时填充 userId
        if (StpUtil.isLogin()) {
            if (metaObject.hasSetter("userId")) { // 检查是否存在 userId 字段
                Object userId = metaObject.getValue("userId");
                // 仅当 userId 为 null 时设置新值，防止覆盖已存在的值
                if (userId == null) {
                    metaObject.setValue("userId", StpUtil.getLoginIdAsLong());
                }
                if (userId instanceof Long uid) {
                    if(uid == -1 || uid == 0)
                        metaObject.setValue("userId", StpUtil.getLoginIdAsLong());
                }
                if (userId instanceof String uid) {
                    if(uid.equals("-1") || uid.equals("0"))
                        metaObject.setValue("userId", StpUtil.getLoginIdAsString());
                }
            }
        }
    }


    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }

}
