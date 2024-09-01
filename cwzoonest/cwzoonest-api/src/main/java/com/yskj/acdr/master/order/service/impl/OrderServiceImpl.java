package com.yskj.acdr.master.order.service.impl;

import com.yskj.acdr.master.order.entity.Order;
import com.yskj.acdr.master.order.mapper.OrderMapper;
import com.yskj.acdr.master.order.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 林河
 * @since 2024-08-15
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

}
