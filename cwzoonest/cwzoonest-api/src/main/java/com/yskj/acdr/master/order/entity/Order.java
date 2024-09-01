package com.yskj.acdr.master.order.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 订单实体类
 * </p>
 *
 * @author 林河
 * @since 2024-08-15
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("acdr_order")
@ApiModel(value = "Order对象", description = "订单实体类")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("预约表主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("预约创建用户")
    @TableField(value = "user_id", fill = FieldFill.INSERT)
    private Integer userId;

    @ApiModelProperty("预约时间")
    @TableField("reservation_time")
    @NotNull(message = "预约时间不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reservationTime;

    @ApiModelProperty("被预约的服务主键")
    @TableField("personal_service_id")
    @NotNull(message = "预约服务ID不能为空")
    private Long personalServiceId;

    @ApiModelProperty("接受服务的该id")
    @TableField("personal_service_user_id")
    @NotNull(message = "服务宠托师ID不能为空")
    private Long personalServiceUserId;

    @ApiModelProperty("本次预约的支付金额")
    @TableField("price")
    @NotNull(message = "支付金额")
    @DecimalMin(value = "0.0", inclusive = false, message = "支付金额必须大于0")
    @DecimalMax(value = "1000.0", message = "支付金额不能超过1000")
    private BigDecimal price;

    @ApiModelProperty("本次预约是否支付")
    @TableField("is_pay")
    private Integer isPay;

    @ApiModelProperty("本次预约的反馈")
    @TableField("feedback")
    private String feedback;

    @ApiModelProperty("本次预约的评分")
    @TableField("star")
    private Integer star;

    @ApiModelProperty("本次预约的状态(0:等待开始，1:进行中,2:结束,3:待支付,4:已完成,6:已评论)")
    @TableField("state")
    private Integer state = 0;

    @ApiModelProperty("支付工具")
    @TableField("payment_method")
    private Long paymentMethod;

    @ApiModelProperty("预约创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("预约更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("客户下单地址")
    @TableField("address")
    @NotNull(message = "用户下单地址不能为空")
    private Long address;

    @ApiModelProperty("客户下单宠物信息")
    @TableField("pet")
    @NotNull(message = "用户下单宠物不能为空")
    private Long pet;

    @ApiModelProperty("客户下单备注信息")
    @TableField("remark")
    private String remark;
}
