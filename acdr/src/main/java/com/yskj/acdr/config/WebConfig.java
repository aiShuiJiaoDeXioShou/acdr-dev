package com.yskj.acdr.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * SpringBoot全局配置
 *
 * @author hjp
 * @apiNote 包含鉴权拦截器、CORS跨域、MP分页插件、Redis配置等
 * @since 2024-07-10
 */
@Slf4j
@Configuration
@EnableScheduling
public class WebConfig implements WebMvcConfigurer {

    @Value("${path.profile}")
    private String profile;

    @Value("${path.file}")
    private String pathFile;

    /**
     * 注册 Sa-Token 拦截器，打开注解式鉴权
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
                .addPathPatterns("/**")
                .excludePathPatterns("/others/**")
                .excludePathPatterns("/public/**")
                .excludePathPatterns("/profile/**")
                .excludePathPatterns("/userInfo/selectPuk")
                .excludePathPatterns("/userInfo/register")
                .excludePathPatterns("/userInfo/getCode");
    }


    /**
     * 注册 Mybatis-Plus 拦截器，配置分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    /**
     * 配置Redis序列化方式
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 解决key序列化方式
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);

        // 解决value序列化
        GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        redisTemplate.setValueSerializer(jsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jsonRedisSerializer);

        return redisTemplate;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 将自定义的静态资源目录映射到 URL 路径
        registry.addResourceHandler("/profile/**")
                .addResourceLocations("file:" + profile)
                .addResourceLocations("file:" + pathFile);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 允许跨域的路径
                .allowedOrigins("*") // 允许跨域的域名，可以多个
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的方法
                .allowedHeaders("*") // 允许的请求头
                //.allowCredentials(true) // 是否允许发送Cookie信息
                .maxAge(3600); // 预检请求的缓存时间（以秒为单位）
    }
}
