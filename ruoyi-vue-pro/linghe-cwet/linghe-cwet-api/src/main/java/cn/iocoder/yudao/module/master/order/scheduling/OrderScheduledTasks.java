package cn.iocoder.yudao.module.master.order.scheduling;

import cn.iocoder.yudao.module.master.order.service.OrderService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderScheduledTasks {

    @Resource
    private OrderService orderService;

    // 每隔5秒钟执行一次,在上一次执行完之后,等五秒再次执行
    // @Scheduled(fixedDelay = 5000)
    public void runTaskWithFixedDelay() {
        // 检查订单状态并且更新
        orderService.checkStateAndUpdate();
    }
}
