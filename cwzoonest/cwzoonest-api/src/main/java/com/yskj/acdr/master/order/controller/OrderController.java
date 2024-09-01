package com.yskj.acdr.master.order.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.yskj.acdr.master.order.entity.Order;
import com.yskj.acdr.master.order.service.IOrderService;
import com.yskj.acdr.common.response.GlobalResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import jakarta.validation.groups.Default;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 林河
 * @since 2024-08-15
 */
@Api(tags = "订单业务逻辑")
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private IOrderService service;

    @ApiOperation(value = "分页列表", response = Order.class)
    @PostMapping(value = "/my")
    public GlobalResponse<Order> list(GlobalResponse<Order> page) {
        return service.lambdaQuery()
                .eq(Order::getUserId, StpUtil.getLoginIdAsLong())
                .page(page);
    }

    @ApiOperation(value = "详情", response = Order.class)
    @GetMapping(value = "/info/{id}")
    public GlobalResponse<Order> info(@Validated({GetMapping.class}) @PathVariable Long id) {
        Order order = service.getById(id);
        return GlobalResponse.success(order);
    }

    @ApiOperation(value = "订单创建与支付")
    @PostMapping(value = "/pay")
    public GlobalResponse<Order> add(
            @Validated({PostMapping.class, Default.class})
            @RequestBody Order param) {
        service.save(param);
        return GlobalResponse.success("新增成功！");
    }
}
