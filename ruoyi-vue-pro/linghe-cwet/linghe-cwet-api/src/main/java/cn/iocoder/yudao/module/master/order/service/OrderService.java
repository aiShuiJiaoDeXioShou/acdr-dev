package cn.iocoder.yudao.module.master.order.service;

import cn.iocoder.yudao.module.master.order.entity.PetOrder;
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
public interface OrderService extends IService<PetOrder> {

    Map<String, Object> orderInfo(PetOrder order);

    // 判断该宠托师是否已经被预约
    boolean isBooking(PetOrder order);

    // 检查订单状态并且更新
    boolean checkStateAndUpdate();
}
