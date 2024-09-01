package cn.iocoder.yudao.module.common.response;

import cn.iocoder.yudao.module.common.constant.GlobalConstant;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 接口响应封装
 *
 * @author hjp
 * @apiNote 非分页时序列化忽略【current、size、pages、total】四个属性
 * @since 2024-07-10
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = GlobalResponse.ValueFilter.class)
public class GlobalResponse<T> implements IPage<T> {

    @Serial
    private static final long serialVersionUID = -724026286707523869L;

    /**
     * 状态码
     */
    private int code = GlobalConstant.CODE_100;
    /**
     * 状态描述
     */
    private String codeStr = "-";
    /**
     * 提示信息
     */
    private String message = "-";
    /**
     * 当前页（默认-1不分页，不分页时不返回）
     */
    private long current = -1;
    /**
     * 每页显示记录数（默认-1不分页，不分页时不返回）
     */
    private long size = -1;
    /**
     * 总页数（不分页时不返回）
     */
    private long pages = -1;
    /**
     * 总记录数（不分页时不返回）
     */
    private long total = -1;
    /**
     * 查询数据列表
     */
    private List<T> records = Collections.emptyList();

    /**
     * 返回的数据首列
     */
    private T data = null;

    public GlobalResponse() {
    }

    public GlobalResponse(long current, long size) {
        this.current = current;
        this.size = size;
    }

    public GlobalResponse<T> setCode(int code) {
        this.code = code;
        this.codeStr = GlobalResponse.codeStr(code);
        return this;
    }

    public String getMessage() {
        return message == null ? codeStr : message;
    }

    @Override
    public long getCurrent() {
        return size < 0 ? -1 : current;
    }

    @Override
    public long getSize() {
        return current < 0 ? -1 : size;
    }

    @Override
    public long getPages() {
        if (pages == -1 && size > 0) {
            pages = total / size;
            if (total % size != 0) {
                pages += 1;
            }
        }
        return pages;
    }

    public GlobalResponse<T> setRecord(T record) {
        if (record != null) {
            this.records = new ArrayList<>(1);
            records.add(record);
        }
        return setRecords(this.records);
    }

    @Override
    public GlobalResponse<T> setRecords(List<T> records) {
        if (records == null || records.isEmpty()) {
            // 心智太重了返回两百吧
            // return setCode(CODE_204).setMessage(SELECT_NO_DATA);
            return setCode(GlobalConstant.CODE_200).setMessage(GlobalConstant.SELECT_NO_DATA);
        } else {
            this.records = records;
            this.data = records.get(0);
            return setCode(GlobalConstant.CODE_200).setMessage(GlobalConstant.SELECT_OK);
        }
    }

    @Override
    public List<OrderItem> orders() {
        return Collections.emptyList();
    }

    // ================================================S 响应静态方法 S================================================

    /**
     * 响应200/400
     *
     * @param flag    响应状态
     * @param message 响应消息
     */
    public static <T> GlobalResponse<T> response(boolean flag, String message) {
        if (flag) {
            return success(message + GlobalConstant.SUCCESS);
        } else {
            return failure(message + GlobalConstant.FAILURE);
        }
    }

    /**
     * 响应200/400
     *
     * @param flag    响应状态
     * @param message 响应消息
     * @param record  响应数据
     */
    public static <T> GlobalResponse<T> response(boolean flag, String message, T record) {
        if (flag) {
            return success(message + GlobalConstant.SUCCESS, record);
        } else {
            return failure(message + GlobalConstant.FAILURE, record);
        }
    }

    /**
     * 响应200/400
     *
     * @param flag    响应状态
     * @param message 响应消息
     * @param records 响应数据
     */
    public static <T> GlobalResponse<T> response(boolean flag, String message, List<T> records) {
        if (flag) {
            return success(message + GlobalConstant.SUCCESS, records);
        } else {
            return failure(message + GlobalConstant.FAILURE, records);
        }
    }

    /**
     * 响应
     *
     * @param flag    响应状态
     * @param code    响应码
     * @param message 响应消息
     */
    public static <T> GlobalResponse<T> response(boolean flag, int code, String message) {
        if (flag) {
            return success(code, message + GlobalConstant.SUCCESS);
        } else {
            return failure(code, message + GlobalConstant.FAILURE);
        }
    }

    /**
     * 响应
     *
     * @param flag    响应状态
     * @param code    响应码
     * @param message 响应消息
     * @param record  响应数据
     */
    public static <T> GlobalResponse<T> response(boolean flag, int code, String message, T record) {
        if (flag) {
            return success(code, message + GlobalConstant.SUCCESS, record);
        } else {
            return failure(code, message + GlobalConstant.FAILURE, record);
        }
    }

    /**
     * 响应
     *
     * @param flag    响应状态
     * @param code    响应码
     * @param message 响应消息
     * @param records 响应数据
     */
    public static <T> GlobalResponse<T> response(boolean flag, int code, String message, List<T> records) {
        if (flag) {
            return success(code, message + GlobalConstant.SUCCESS, records);
        } else {
            return failure(code, message + GlobalConstant.FAILURE, records);
        }
    }

    // ================================================E 响应静态方法 E================================================

    // ================================================S 成功静态方法 S================================================

    /**
     * 成功200
     *
     * @param message 响应消息
     */
    public static <T> GlobalResponse<T> success(String message) {
        return new GlobalResponse<T>()
                .setMessage(message)
                .setCode(GlobalConstant.CODE_200);
    }

    /**
     * 成功200
     *
     */
    public static <T> GlobalResponse<T> success() {
        return new GlobalResponse<T>()
                .setMessage("成功！")
                .setCode(GlobalConstant.CODE_200);
    }

    /**
     * 成功200
     *
     * @param record  响应数据
     */
    public static <T> GlobalResponse<T> success(T record) {
        return new GlobalResponse<T>()
                .setRecord(record)
                .setMessage("成功！")
                .setCode(GlobalConstant.CODE_200);
    }

    /**
     * 成功200
     *
     * @param message 响应消息
     * @param record  响应数据
     */
    public static <T> GlobalResponse<T> success(String message, T record) {
        return new GlobalResponse<T>()
                .setRecord(record)
                .setMessage(message)
                .setCode(GlobalConstant.CODE_200);
    }

    /**
     * 成功200
     *
     * @param message 响应消息
     * @param records 响应数据
     */
    public static <T> GlobalResponse<T> success(String message, List<T> records) {
        return new GlobalResponse<T>()
                .setRecords(records)
                .setMessage(message)
                .setCode(GlobalConstant.CODE_200);
    }

    /**
     * 成功
     *
     * @param code    响应码
     * @param message 响应消息
     */
    public static <T> GlobalResponse<T> success(int code, String message) {
        return new GlobalResponse<T>()
                .setMessage(message)
                .setCode(code);
    }

    /**
     * 成功
     *
     * @param code    响应码
     * @param message 响应消息
     * @param record  响应数据
     */
    public static <T> GlobalResponse<T> success(int code, String message, T record) {
        return new GlobalResponse<T>()
                .setRecord(record)
                .setMessage(message)
                .setCode(code);
    }

    /**
     * 成功
     *
     * @param code    响应码
     * @param message 响应消息
     * @param records 响应数据
     */
    public static <T> GlobalResponse<T> success(int code, String message, List<T> records) {
        return new GlobalResponse<T>()
                .setRecords(records)
                .setMessage(message)
                .setCode(code);
    }

    // ================================================E 成功静态方法 E================================================

    // ================================================S 失败静态方法 S================================================

    /**
     * 失败400
     */
    public static <T> GlobalResponse<T> failure() {
        return new GlobalResponse<T>()
                .setMessage("失败！")
                .setCode(GlobalConstant.CODE_400);
    }

    /**
     * 失败400
     *
     * @param message 响应消息
     */
    public static <T> GlobalResponse<T> failure(String message) {
        return new GlobalResponse<T>()
                .setMessage(message)
                .setCode(GlobalConstant.CODE_400);
    }

    /**
     * 失败400
     *
     * @param message 响应消息
     * @param record  响应数据
     */
    public static <T> GlobalResponse<T> failure(String message, T record) {
        return new GlobalResponse<T>()
                .setRecord(record)
                .setMessage(message)
                .setCode(GlobalConstant.CODE_400);
    }

    /**
     * 失败400
     *
     * @param message 响应消息
     * @param records 响应数据
     */
    public static <T> GlobalResponse<T> failure(String message, List<T> records) {
        return new GlobalResponse<T>()
                .setRecords(records)
                .setMessage(message)
                .setCode(GlobalConstant.CODE_400);
    }

    /**
     * 失败
     *
     * @param code    响应码
     * @param message 响应消息
     */
    public static <T> GlobalResponse<T> failure(int code, String message) {
        return new GlobalResponse<T>()
                .setMessage(message)
                .setCode(code);
    }

    /**
     * 失败
     *
     * @param code    响应码
     * @param message 响应消息
     * @param record  响应数据
     */
    public static <T> GlobalResponse<T> failure(int code, String message, T record) {
        return new GlobalResponse<T>()
                .setRecord(record)
                .setMessage(message)
                .setCode(code);
    }

    /**
     * 失败
     *
     * @param code    响应码
     * @param message 响应消息
     * @param records 响应数据
     */
    public static <T> GlobalResponse<T> failure(int code, String message, List<T> records) {
        return new GlobalResponse<T>()
                .setRecords(records)
                .setMessage(message)
                .setCode(code);
    }

    // ================================================E 失败静态方法 E================================================

    /**
     * 状态码附带消息
     *
     * @param state 状态码
     * @return 附带消息
     */
    protected static String codeStr(int state) {
        return switch (state) {
            case GlobalConstant.CODE_200 ->
                // 服务器已成功处理了请求（有返回内容）
                    "成功";
            case GlobalConstant.CODE_201 ->
                // 请求成功并且服务器创建了新的资源（比如说上传文件）
                    "已创建";
            case GlobalConstant.CODE_202 ->
                // 服务器已接受请求，但尚未处理完成
                    "已接受";
            case GlobalConstant.CODE_204 ->
                // 服务器成功处理了请求，但是没有需要返回的内容
                    "无内容";
            case GlobalConstant.CODE_400 ->
                // 服务器无法解析该请求
                    "错误请求";
            case GlobalConstant.CODE_401 ->
                // 请求没有进行身份验证或验证未通过（Token验证失败、登录失败等）
                    "未授权";
            case GlobalConstant.CODE_403 ->
                // 服务器拒绝此请求（比如使用普通用户的 Token 去请求管理员才能访问的资源）
                    "禁止访问";
            case GlobalConstant.CODE_404 ->
                // 服务器找不到请求的内容
                    "未找到";
            case GlobalConstant.CODE_405 ->
                // 错误的请求方式
                    "错误的请求方式";
            case GlobalConstant.CODE_408 ->
                // 服务器请求超时
                    "请求超时";
            case GlobalConstant.CODE_410 ->
                // 请求的资源已永久删除
                    "已删除";
            case GlobalConstant.CODE_413 ->
                // 请求实体过大，超出服务器的处理能力
                    "请求实体过大";
            case GlobalConstant.CODE_500 ->
                // 服务器遇到错误，无法完成请求
                    "系统异常";
            case GlobalConstant.CODE_501 ->
                // 服务器不具备完成请求的功能（比如功能未完成开发）
                    "未实现";
            case GlobalConstant.CODE_502 ->
                // 服务器作为网关或代理，从上游服务器收到无效响应（比如调用第三方接口出现问题）
                    "错误网关";
            case GlobalConstant.CODE_503 ->
                // 服务器作为网关或代理，从上游服务器收到无效响应（比如调用第三方接口出现问题）
                    "已被限流";
            default -> "-";
        };
    }

    /**
     * 序列化过滤器
     */
    protected static class ValueFilter {
        @Override
        public boolean equals(Object obj) {
            boolean flag = false;
            if (obj == null) {
                flag = true;
            } else {
                String str = obj.toString();
                if (str.isEmpty()) {
                    flag = true;
                } else {
                    if (obj instanceof Long) {
                        long l = Long.parseLong(str);
                        if (l < 0) {
                            flag = true;
                        }
                    }
                }
            }
            return flag;
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }
    }
}

