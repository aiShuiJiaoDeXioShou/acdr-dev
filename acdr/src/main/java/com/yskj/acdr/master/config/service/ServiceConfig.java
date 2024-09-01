package com.yskj.acdr.master.config.service;

import com.yskj.acdr.master.config.entity.Config;
import com.yskj.acdr.master.config.mapper.ConfigMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Getter;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 林河
 * @since 2024-08-22
 */
@Service
@Getter
public class ServiceConfig extends ServiceImpl<ConfigMapper, Config> {

    // 设置二维码过期时间
    private long serviceExpiredTime = 24;

}
