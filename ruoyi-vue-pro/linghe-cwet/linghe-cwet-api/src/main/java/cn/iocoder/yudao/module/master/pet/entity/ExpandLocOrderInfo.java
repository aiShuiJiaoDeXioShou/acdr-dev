package cn.iocoder.yudao.module.master.pet.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 拓展位订单信息实体类
 *
 * @author hjp
 * @since 2024-07-22
 */
@TableName(value = "sys_expand_loc_order_info")
@Getter
@Setter
@Accessors(chain = true)
public class ExpandLocOrderInfo {
    /**
     * 宠物拓展位主键（只记录支付成功的）
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 订单号
     */
    @TableField(value = "number")
    private String number;

    /**
     * 用户主键
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 支付完成时间
     */
    @TableField(value = "time")
    private Date time;

    /**
     * 订单金额
     */
    @TableField(value = "amount")
    private BigDecimal amount;

    /**
     * 用户手机号
     */
    @TableField(value = "phone")
    private String phone;

}
