package com.yskj.acdr.common.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 容器监听器（启动、关闭）
 *
 * @author hjp
 * @since 2024-07-10
 */
@Slf4j
@Component
public class GlobalModuleListener implements ServletContextListener {

    /**
     * 容器启动
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("Servlet Context Event - Initialized");
    }

    /**
     * 容器关闭
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("Servlet Context Event - Destroyed");
    }

}
