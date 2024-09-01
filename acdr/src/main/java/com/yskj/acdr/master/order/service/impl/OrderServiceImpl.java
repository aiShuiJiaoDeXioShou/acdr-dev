package com.yskj.acdr.master.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yskj.acdr.master.address.entity.ChinaAddress;
import com.yskj.acdr.master.address.service.ChinaAddressService;
import com.yskj.acdr.master.order.entity.Order;
import com.yskj.acdr.master.order.mapper.OrderMapper;
import com.yskj.acdr.master.order.orderenum.OrderState;
import com.yskj.acdr.master.order.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yskj.acdr.master.personal.entity.PersonalService;
import com.yskj.acdr.master.personal.service.PersonalServiceService;
import com.yskj.acdr.master.pet.entity.PetInfo;
import com.yskj.acdr.master.pet.service.PetInfoService;
import com.yskj.acdr.master.user.entity.Users;
import com.yskj.acdr.master.user.service.UsersService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 林河
 * @since 2024-08-15
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Resource
    private ChinaAddressService addressService;

    @Resource
    private PetInfoService petInfoService;

    @Resource
    private UsersService usersService;

    @Resource
    private PersonalServiceService personalServiceService;

    @Override
    public Map<String, Object> orderInfo(Order order) {
        Map<String, Object> obj = BeanUtil.beanToMap(order);
        PetInfo pet = petInfoService.getById((Long) obj.get("pet"));
        obj.put("pet", pet);
        ChinaAddress address = addressService.getById((Long) obj.get("address"));
        obj.put("address", address);
        obj.put("user", usersService.getSafeUsers());
        Users psUser = usersService.getSafeUsers((Long) obj.get("personalServiceUserId"));
        obj.put("psUser", psUser);
        // 获取当前服务的信息
        PersonalService ps = personalServiceService.getById((Long) obj.get("personalServiceId"));
        obj.put("serviceInfo", ps);
        return obj;
    }

    // 判断该宠托师是否已经被预约
    @Override
    public boolean isBooking(Order order) {
        // 判断当前被预约用户是否有时间, 没有时间就返回相关错误信息
        var wrapper = new LambdaQueryWrapper<Order>();
        LocalDateTime rtInterval = order.getReservationTime();
        LocalDateTime addAfterTime = rtInterval.plusHours(order.getServiceHours());
        wrapper.eq(Order::getPersonalServiceUserId, order.getPersonalServiceUserId())
                // 判断当前订单状态
                .and(l-> l.ge(Order::getState, OrderState.PAYED)
                        .le(Order::getState, OrderState.EVALUATED))
                // 判断预约时间是否正确
                .and(l-> l.ge(Order::getReservationTime, order.getReservationTime())
                        .le(Order::getReservationTime, addAfterTime));
        Order one = this.getOne(wrapper);
        return one != null;
    }

    // 检查订单状态并且更新
    @Override
    public boolean checkStateAndUpdate() {
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        LambdaUpdateWrapper<Order> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Order::getState, OrderState.CANCELED)
                .eq(Order::getState, OrderState.WAIT_PAY)
                // 判断当前时间是不是大于订单过期时间
                .apply("DATE_ADD(create_time, INTERVAL 15 MINUTE) < {0}", now);
        // 执行更新操作
        return this.update(null, updateWrapper);
    }

}
