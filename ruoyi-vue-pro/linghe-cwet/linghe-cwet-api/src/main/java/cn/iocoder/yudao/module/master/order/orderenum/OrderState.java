package cn.iocoder.yudao.module.master.order.orderenum;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum OrderState {

    /**
     * 待支付
     */
    WAIT_PAY(0, "待支付"),

    /**
     * 已支付
     */
    PAYED(1, "已支付"),

    /**
     * 已预约/已发货
     */
    SHIPPED(2, "已预约/已发货"),

    /**
     * 已完成
     */
    FINISHED(3, "已完成"),

    /**
     * 已评价
     */
    EVALUATED(4, "已评价"),

    /**
     * 退款中
     */
    REFUNDING(-2, "退款中"),

    /**
     * 已退款
     */
    REFUNDED(-3, "已退款"),

    /**
     * 已取消
     */
    CANCELED(-1, "已取消"),

    /**
     * 被商家/宠托师取消
     */
    CANCELED_BY_MERCHANT(-4, "被商家/宠托师取消");

    @EnumValue
    private final int value;
    @JsonValue
    private final String info;

    OrderState(int value, String info) {
        this.value = value;
        this.info = info;
    }

    /**
     * 根据枚举值获取对应的中文提示信息
     */
    public static String getInfoByValue(int value) {
        for (OrderState state : OrderState.values()) {
            if (state.getValue() == value) {
                return state.getInfo();
            }
        }
        return "未知状态";
    }
}

