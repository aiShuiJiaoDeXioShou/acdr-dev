package com.yskj.acdr.master.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * author: hjp
 */
@Component
@ConfigurationProperties(prefix = "sky.wechat")
@Getter
@Setter
public class WeChatProperties {
    /**
     * 小程序的appid
     */
    private String appid;
    /**
     * 小程序的密钥
     */
    private String secret;

}
