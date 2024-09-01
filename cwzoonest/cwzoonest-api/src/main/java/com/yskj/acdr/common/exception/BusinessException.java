package com.yskj.acdr.common.exception;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

import static com.yskj.acdr.common.constant.GlobalConstant.CODE_500;


/**
 * 业务异常
 *
 * @author hjp
 * @since 2024-07-10
 */
@Getter
@Setter
public class BusinessException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -2248538883755819627L;

    /**
     * 获取异常的错误码
     */
    private int code;

    /**
     * 构造函数，根据错误码、错误消息和报错原因创建业务异常对象
     *
     * @param message 错误消息
     */
    public BusinessException(int code, String message, Throwable throwable) {
        super(message, throwable);
        this.code = code;
    }

    /**
     * 构造函数，根据错误码、错误消息和报错原因创建业务异常对象
     *
     * @param message 错误消息
     */
    public BusinessException(String message, Throwable throwable) {
        super(message, throwable);
        this.code = CODE_500;
    }

    /**
     * 构造函数，根据错误码、错误消息和报错原因创建业务异常对象
     *
     * @param message 错误消息
     */
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 构造函数，根据错误消息和报错原因创建业务异常对象
     *
     * @param message 错误消息
     */
    public BusinessException(String message) {
        super(message);
        this.code = CODE_500;
    }

}

