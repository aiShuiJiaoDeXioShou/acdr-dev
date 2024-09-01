package cn.iocoder.yudao.module.common.handler;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.module.common.aspect.GlobalLogsAspect;
import cn.iocoder.yudao.module.common.exception.BusinessException;
import cn.iocoder.yudao.module.common.exception.LimitException;
import cn.iocoder.yudao.module.common.exception.LoginFailedException;
import cn.iocoder.yudao.module.common.response.GlobalResponse;
import jakarta.annotation.Resource;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.Set;

import static cn.iocoder.yudao.module.common.constant.GlobalConstant.*;


/**
 * 更详细的全局异常处理
 *
 * @author hjp
 * @since 2024-07-10
 */
@Slf4j
@RestControllerAdvice
public class PetGlobalExceptionHandler {

    @Resource
    private GlobalLogsAspect globalLogsAspect;

    /**
     * 处理业务异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BusinessException.class)
    public <T> GlobalResponse<T> handleBusinessException(BusinessException ex) {
        return GlobalResponse.failure(ex.getCode(), "业务异常: " + ex.getMessage());
    }

    /**
     * 处理缺少必要参数的异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public <T> GlobalResponse<T> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        return GlobalResponse.failure(CODE_500, "缺少请求参数: " + ex.getParameterName());
    }

    /**
     * 处理方法参数校验错误的异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public <T> GlobalResponse<T> handleConstraintViolationException(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        StringBuilder message = new StringBuilder();
        for (ConstraintViolation<?> violation : violations) {
            message.append("；").append(violation.getMessage());
        }
        return GlobalResponse.failure(CODE_400, message.deleteCharAt(0).toString());
    }

    /**
     * 处理方法参数校验错误的异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public <T> GlobalResponse<T> handleMethodArgumentNotValidException(BindException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        StringBuilder message = new StringBuilder();
        for (FieldError error : fieldErrors) {
            message.append("；").append(error.getDefaultMessage());
        }
        return GlobalResponse.failure(CODE_400, message.deleteCharAt(0).toString());
    }

    /**
     * 处理参数类型不匹配的异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TypeMismatchException.class)
    public <T> GlobalResponse<T> handleTypeMismatchException(TypeMismatchException ex) {
        String message = StrUtil.format("请求参数类型错误: {}应为{}", ex.getPropertyName(), ex.getRequiredType());
        return GlobalResponse.failure(CODE_400, message);
    }

    /**
     * 处理找不到请求地址的异常
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public <T> GlobalResponse<T> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        String message = StrUtil.format("找不到请求地址（{}）: {}", globalLogsAspect.getClientIp(), ex.getRequestURL());
        log.error(message);
        return GlobalResponse.failure(CODE_404, message);
    }

    /**
     * 处理找不到请求资源的异常
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoResourceFoundException.class)
    public <T> GlobalResponse<T> handleNoResourceFoundException(NoResourceFoundException ex) {
        String message = StrUtil.format("找不到请求资源（{}）: {}", globalLogsAspect.getClientIp(), ex.getResourcePath());
        log.error(message);
        return GlobalResponse.failure(CODE_404, message);
    }

    /**
     * 处理上传超过允许的最大限制
     */
    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public <T> GlobalResponse<T> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        String message = "上传超过允许的最大限制：" + ex.getMaxUploadSize();
        log.error(message);
        return GlobalResponse.failure(CODE_413, message);
    }

    /**
     * 处理不支持的HTTP请求方法异常
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public <T> GlobalResponse<T> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return GlobalResponse.failure(CODE_405, "不支持的请求方法: " + ex.getMethod());
    }

    /**
     * 处理空指针异常
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(NullPointerException.class)
    public <T> GlobalResponse<T> handleNullPointerException(NullPointerException ex) {
        log.error(ex.getMessage(), ex);
        return GlobalResponse.failure(CODE_500, "空指针异常: " + ex.getMessage());
    }

    /**
     * 处理运行时异常
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public <T> GlobalResponse<T> handleRuntimeException(RuntimeException ex) {
        log.error(ex.getMessage(), ex);
        return GlobalResponse.failure(CODE_500, "运行时异常: " + ex.getMessage());
    }

    /**
     * 处理运行时异常
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(LimitException.class)
    public <T> GlobalResponse<T> handleLimitException(LimitException ex) {
        log.error(ex.getMessage(), ex);
        return GlobalResponse.failure(CODE_503, ex.getMessage());
    }

    /**
     * 登录异常
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(LoginFailedException.class)
    public <T> GlobalResponse<T> handleLoginFailedException(LoginFailedException ex) {
        log.error(ex.getMessage(), ex);
        return GlobalResponse.failure(CODE_401, ex.getMessage());
    }


    /**
     * 处理其他异常
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public <T> GlobalResponse<T> handleException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return GlobalResponse.failure(CODE_500, "服务器错误: " + ex.getMessage());
    }
}

