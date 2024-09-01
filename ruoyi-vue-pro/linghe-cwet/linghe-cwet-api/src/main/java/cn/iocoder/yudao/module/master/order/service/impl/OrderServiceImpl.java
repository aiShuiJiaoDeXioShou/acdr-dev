package cn.iocoder.yudao.module.master.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.iocoder.yudao.module.master.order.mapper.PetOrderMapper;
import cn.iocoder.yudao.module.master.order.service.OrderService;
import cn.iocoder.yudao.module.master.pet.entity.PetInfo;
import cn.iocoder.yudao.module.master.pet.service.PetInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import cn.iocoder.yudao.module.master.address.entity.ChinaAddress;
import cn.iocoder.yudao.module.master.address.service.ChinaAddressService;
import cn.iocoder.yudao.module.master.order.entity.PetOrder;
import cn.iocoder.yudao.module.master.order.orderenum.OrderState;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.iocoder.yudao.module.master.personal.entity.PersonalService;
import cn.iocoder.yudao.module.master.personal.service.PersonalServiceService;
import cn.iocoder.yudao.module.master.user.entity.Users;
import cn.iocoder.yudao.module.master.user.service.UsersService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

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
public class OrderServiceImpl extends ServiceImpl<PetOrderMapper, PetOrder> implements OrderService {

    @Resource
    private ChinaAddressService addressService;

    @Resource
    private PetInfoService petInfoService;

    @Resource
    private UsersService usersService;

    @Resource
    private PersonalServiceService personalServiceService;

    @Override
    public Map<String, Object> orderInfo(PetOrder order) {
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
    public boolean isBooking(PetOrder order) {
        // 判断当前被预约用户是否有时间, 没有时间就返回相关错误信息
        var wrapper = new LambdaQueryWrapper<PetOrder>();
        LocalDateTime rtInterval = order.getReservationTime();
        LocalDateTime addAfterTime = rtInterval.plusHours(order.getServiceHours());
        wrapper.eq(PetOrder::getPersonalServiceUserId, order.getPersonalServiceUserId())
                // 判断当前订单状态
                .and(l-> l.ge(PetOrder::getState, OrderState.PAYED)
                        .le(PetOrder::getState, OrderState.EVALUATED))
                // 判断预约时间是否正确
                .and(l-> l.ge(PetOrder::getReservationTime, order.getReservationTime())
                        .le(PetOrder::getReservationTime, addAfterTime));
        PetOrder one = this.getOne(wrapper);
        return one != null;
    }

    // 检查订单状态并且更新
    @Override
    public boolean checkStateAndUpdate() {
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        LambdaUpdateWrapper<PetOrder> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(PetOrder::getState, OrderState.CANCELED)
                .eq(PetOrder::getState, OrderState.WAIT_PAY)
                // 判断当前时间是不是大于订单过期时间
                .apply("DATE_ADD(create_time, INTERVAL 15 MINUTE) < {0}", now);
        // 执行更新操作
        return this.update(null, updateWrapper);
    }

}
