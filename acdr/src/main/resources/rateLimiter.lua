-- 限流Lua脚本
-- KEYS[1] 是键，ARGV[1] 是限流次数，ARGV[2] 是限流时间
local key = KEYS[1]
local limit = tonumber(ARGV[1])
local timeout = tonumber(ARGV[2])

-- 当前计数
local current = redis.call('incr', key)
if current == 1 then
    -- 如果是第一次访问，设置过期时间
    redis.call('expire', key, timeout)
    return 1
end

-- 检查是否超过限流
if current > limit then
    return 0
end
return 1
