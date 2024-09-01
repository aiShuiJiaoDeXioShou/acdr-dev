package cn.iocoder.yudao.module.limit;

/**
 * 限流类型
 *
 * @author hjp
 * @since 2024-07-10
 */
public enum LimitType {
    /**
     * 参数限流
     */
    PARAMETER,
    /**
     * 根据请求者IP进行限流
     */
    IP
}
