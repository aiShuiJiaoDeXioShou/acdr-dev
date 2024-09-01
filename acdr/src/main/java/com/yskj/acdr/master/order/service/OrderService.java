package com.yskj.acdr.master.order.service;

import com.yskj.acdr.master.order.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 林河
 * @since 2024-08-15
 */
public interface OrderService extends IService<Order> {

    Map<String, Object> orderInfo(Order order);

    // 判断该宠托师是否已经被预约
    boolean isBooking(Order order);

    // 检查订单状态并且更新
    boolean checkStateAndUpdate();
}
