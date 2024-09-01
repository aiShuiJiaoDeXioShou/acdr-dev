package cn.iocoder.yudao.module.common.exception;

import cn.iocoder.yudao.module.common.constant.GlobalConstant;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * 限流异常
 *
 * @author hjp
 * @since 2024-07-10
 */
@Getter
@Setter
public class LimitException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -2348539083755819627L;

    /**
     * 获取异常的错误码
     */
    private int code;

    /**
     * 构造函数，根据错误码、错误消息和报错原因创建业务异常对象
     *
     * @param message 错误消息
     */
    public LimitException(int code, String message, Throwable throwable) {
        super(message, throwable);
        this.code = code;
    }

    /**
     * 构造函数，根据错误码、错误消息和报错原因创建业务异常对象
     *
     * @param message 错误消息
     */
    public LimitException(String message, Throwable throwable) {
        super(message, throwable);
        this.code = GlobalConstant.CODE_503;
    }

    /**
     * 构造函数，根据错误码、错误消息和报错原因创建业务异常对象
     *
     * @param message 错误消息
     */
    public LimitException(int code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 构造函数，根据错误消息和报错原因创建业务异常对象
     *
     * @param message 错误消息
     */
    public LimitException(String message) {
        super(message);
        this.code = GlobalConstant.CODE_503;
    }
}
