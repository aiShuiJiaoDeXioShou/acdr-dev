package com.yskj.acdr.common.cache;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis缓存工具
 *
 * @author hjp
 * @since 2024-07-10
 */
@Component
public class GlobalRedisCache<T> {

    @Resource
    private RedisTemplate<String, T> redisTemplate;

    /**
     * 获取Redis中存储的值
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 对应的值
     */
    public T get(String key, T defaultValue) {
        T value = get(key);
        return value != null ? value : defaultValue;
    }

    /**
     * 获取Redis中存储的值
     *
     * @param key 键
     * @return 对应的值
     */
    public T get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 设置键值对到Redis，默认不设置过期时间
     *
     * @param key   键
     * @param value 值
     */
    public void set(String key, T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置键值对到Redis，并设置过期时间
     *
     * @param key     键
     * @param value   值
     * @param timeout 过期时间
     * @param unit    时间单位
     */
    public void set(String key, T value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * 删除Redis中存储的值
     *
     * @param key 键
     * @return 是否成功删除
     */
    public boolean delete(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    /**
     * 确定给定的键是否存在
     *
     * @param key 键
     * @return 是否存在
     */
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 模糊匹配键
     *
     * @param pattern 匹配键
     * @return 符合匹配键的Set
     */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 增加计数器的值
     *
     * @param key   键
     * @param delta 增加的值
     * @return 增加后的值
     */
    public Long increment(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 获取指定键的剩余时间
     *
     * @param key  键
     * @param unit 时间单位
     * @return 剩余时间
     */
    public Long getExpire(String key, TimeUnit unit) {
        return redisTemplate.getExpire(key, unit);
    }

}
