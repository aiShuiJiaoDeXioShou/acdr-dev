package cn.iocoder.yudao.module.common.listener;


import cn.iocoder.yudao.module.common.encrypt.GlobalEncryptHelper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 框架监听器（启动、关闭）
 *
 * @author hjp
 * @since 2024-07-10
 */
@Slf4j
@Component
public class GlobalModuleMonitor {


    /**
     * 依赖项注入后执行
     */
    @PostConstruct
    public void postConstruct() {
        // 初始化AES加密
        GlobalEncryptHelper.init();
        log.info("SpringBoot Construct - Post");
    }

    /**
     * 依赖项注销前执行
     */
    @PreDestroy
    public void preDestroy() {
        log.info("SpringBoot Construct - Pre");
    }

}
