package com.yskj.acdr.master.order.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.yskj.acdr.master.order.orderenum.PayType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author 林河
 * @since 2024-08-24
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("acdr_pay_info")
@ApiModel(value = "PayInfo对象", description = "")
public class PayInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("支付日志主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("支付的订单号")
    @TableField("payment_order")
    private Long paymentOrder;

    @ApiModelProperty("支付方法")
    @TableField("payment_method")
    private String paymentMethod;

    @ApiModelProperty("支付交易id")
    @TableField("transactionId")
    private String transactionId;

    @ApiModelProperty("支付用户")
    @TableField(value = "user_id", fill = FieldFill.INSERT)
    private Long userId;

    @ApiModelProperty("支付备注")
    @TableField("payment_info")
    private String paymentInfo;

    @ApiModelProperty("支付渠道")
    @TableField("payment_channel")
    private String paymentChannel;

    @ApiModelProperty("支付金额")
    @TableField("amount")
    private BigDecimal amount;

    @ApiModelProperty("对于该用户交易的类型（是支出还是收入）")
    @TableField("type")
    private PayType type;

    @ApiModelProperty("被支付方")
    @TableField("payee")
    private Long payee;

    @ApiModelProperty("支付时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
