package com.yskj.acdr.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 允许跨域的路径
                .allowedOrigins("http://localhost:8081","http://127.0.0.1:8081") // 允许跨域的域名，可以多个
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的方法
                .allowedHeaders("*") // 允许的请求头
                .allowCredentials(true) // 是否允许发送Cookie信息
                .maxAge(3600); // 预检请求的缓存时间（以秒为单位）
    }
}

