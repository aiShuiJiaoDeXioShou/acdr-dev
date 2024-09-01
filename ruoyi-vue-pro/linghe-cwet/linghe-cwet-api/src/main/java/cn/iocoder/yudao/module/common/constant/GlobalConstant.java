package cn.iocoder.yudao.module.common.constant;

/**
 * 全局静态常量
 *
 * @author hjp
 * @since 2024-07-10
 */
public class GlobalConstant {

    // 接口操作类型

    /**
     * 制作
     */
    public static final String CREATE = "制作";
    /**
     * 保存
     */
    public static final String UPSERT = "保存";
    /**
     * 添加
     */
    public static final String INSERT = "添加";
    /**
     * 修改
     */
    public static final String UPDATE = "修改";
    /**
     * 删除
     */
    public static final String DELETE = "删除";
    /**
     * 查询
     */
    public static final String SELECT = "查询";
    /**
     * 导入
     */
    public static final String IMPORT = "导入";
    /**
     * 导出
     */
    public static final String EXPORT = "导出";

    /**
     * 分配
     */
    public static final String ASSIGN = "分配";
    /**
     * 提交
     */
    public static final String SUBMIT = "提交";
    /**
     * 审核
     */
    public static final String VERIFY = "审核";
    /**
     * 驳回
     */
    public static final String REJECT = "驳回";

    // 接口结果类型

    /**
     * 操作成功
     */
    public static final String SUCCESS = "成功";
    /**
     * 操作失败
     */
    public static final String FAILURE = "失败";
    /**
     * 系统异常
     */
    public static final String EXCEPTION = "异常";
    /**
     * 查询成功
     */
    public static final String SELECT_OK = "查询成功";
    /**
     * 查询成功，但无数据
     */
    public static final String SELECT_NO_DATA = "查询成功，但无数据";

    // 状态码

    /**
     * 默认
     */
    public static final int CODE_100 = 100;
    /**
     * 成功
     */
    public static final int CODE_200 = 200;
    /**
     * 已创建
     */
    public static final int CODE_201 = 201;
    /**
     * 已接受
     */
    public static final int CODE_202 = 202;
    /**
     * 无内容
     */
    public static final int CODE_204 = 204;
    /**
     * 错误请求
     */
    public static final int CODE_400 = 400;
    /**
     * 未授权
     */
    public static final int CODE_401 = 401;
    /**
     * 禁止访问
     */
    public static final int CODE_403 = 403;
    /**
     * 未找到
     */
    public static final int CODE_404 = 404;
    /**
     * 错误的请求方式
     */
    public static final int CODE_405 = 405;
    /**
     * 请求超时
     */
    public static final int CODE_408 = 408;
    /**
     * 已删除
     */
    public static final int CODE_410 = 410;
    /**
     * 请求实体过大
     */
    public static final int CODE_413 = 413;
    /**
     * 系统异常
     */
    public static final int CODE_500 = 500;
    /**
     * 未实现
     */
    public static final int CODE_501 = 501;
    /**
     * 错误网关
     */
    public static final int CODE_502 = 502;
    /**
     * 被限流
     */
    public static final int CODE_503 = 503;
}
