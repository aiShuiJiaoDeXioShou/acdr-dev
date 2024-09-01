package com.yskj.acdr.master.order.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.yskj.acdr.master.address.service.ChinaAddressService;
import com.yskj.acdr.master.config.service.ServiceConfig;
import com.yskj.acdr.master.file.entity.FileMap;
import com.yskj.acdr.master.file.service.FileMapService;
import com.yskj.acdr.master.order.entity.Order;
import com.yskj.acdr.master.order.entity.PayInfo;
import com.yskj.acdr.master.order.entity.QrCode;
import com.yskj.acdr.master.order.mapper.QrCodeMapper;
import com.yskj.acdr.master.order.orderenum.OrderState;
import com.yskj.acdr.master.order.orderenum.PayType;
import com.yskj.acdr.master.order.service.OrderService;
import com.yskj.acdr.common.response.GlobalResponse;
import com.yskj.acdr.master.order.service.PayInfoService;
import com.yskj.acdr.master.personal.service.PersonalServiceService;
import com.yskj.acdr.master.pet.service.PetInfoService;
import com.yskj.acdr.master.user.service.UsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
@Validated
@Slf4j
public class OrderController {

    @Resource
    private FileMapService fileService;

    @Value("${sky.wechat.appid}")
    private String appId;

    @Resource
    private ChinaAddressService addressService;

    @Resource
    private PetInfoService petInfoService;

    @Resource
    private UsersService usersService;

    @Resource
    private PersonalServiceService personalServiceService;

    @Resource
    private OrderService orderService;

    @Resource
    private PayInfoService payService;

    @Resource
    private ServiceConfig serviceConfig;

    @Resource
    private QrCodeMapper qrCodeMapper;

    @ApiOperation(value = "详情", response = Order.class)
    @GetMapping(value = "/info/{id}")
    public GlobalResponse<Map<String, Object>> info(
            @PathVariable Long id) {
        Map<String, Object> order = orderService.orderInfo(orderService.getById(id));
        return GlobalResponse.success(order);
    }

    @ApiOperation(value = "订单创建,服务预约")
    @PostMapping(value = "/create")
    @Transactional(rollbackFor = Exception.class)
    public GlobalResponse<Map<Object, Object>> create(
            @Validated({PostMapping.class, Default.class})
            @RequestBody Order order) {

        // 判断当前被预约用户是否有时间, 没有时间就返回相关错误信息
        if (orderService.isBooking(order)) {
            return GlobalResponse.failure("该宠托师已经有预约时间了");
        }

        // 不能让顾客预约到自己创建的服务
        if (order.getPersonalServiceUserId().equals(StpUtil.getLoginIdAsLong())) {
            return GlobalResponse.failure("你不能预约自己创建的服务！");
        }

        order.setUserId(StpUtil.getLoginIdAsLong())
                .setState(OrderState.WAIT_PAY);
        // 判断创建订单是否成功
        boolean createOrder = orderService.save(order);
        // 获取该创建的订单
        Order nowOrder = orderService.getById(order);
        var map = new HashMap<String, Serializable>(
                Map.of(
                        "appId", appId,
                        "provider", "wxpay",
                        "timeStamp", DateUtil.current(),
                        "nonceStr", RandomUtil.randomString(32),
                        "package", "prepay_id=WXPay",
                        "signType", "MD5"
                )
        );
        String keyvalue = MapUtil.join(map, "&", "=").toUpperCase();
        map.put("paySign", DigestUtils.md5DigestAsHex(keyvalue.getBytes(StandardCharsets.UTF_8)).toUpperCase());
        if (!createOrder) {
            return GlobalResponse.failure("创建订单失败");
        }
        // 创建订单成功
        return GlobalResponse.success("新增成功！", MapUtil.ofEntries(
                MapUtil.entry("payData", map),
                MapUtil.entry("orderData", nowOrder)
        ));
    }

    @ApiOperation(value = "订单支付")
    @PostMapping(value = "/pay/{orderId}")
    @Transactional(rollbackFor = Exception.class)
    public GlobalResponse<String> pay(@PathVariable Long orderId,
                                      // 这个是发送支付的信息过来
                                      @RequestBody Map<String, Object> payData) {

        // 获取该支付的订单号
        Order order = orderService.getById(orderId);
        // 判断订单的状态，如果不为等待支付则返回错误
        if (!order.getState().equals(OrderState.WAIT_PAY)) {
            return GlobalResponse.failure("您无法支付已经取消或者已经支付的订单！");
        }
        if (order.getState().equals(OrderState.CANCELED)) {
            return GlobalResponse.failure("该订单已取消请重新生成");
        }
        order.setState(OrderState.PAYED)
                .setIsPay(true);
        boolean update = orderService.updateById(order);
        if (update) {
            return GlobalResponse.failure("支付失败！");
        }
        // 保存每一笔支付日志
        payService.save(new PayInfo()
                .setAmount(order.getPrice())
                .setPaymentOrder(order.getId())
                .setPayee(order.getPersonalServiceUserId())
                .setPaymentInfo("宠托师服务支付")
                .setType(PayType.EXPENSE));
        return GlobalResponse.success("订单支付成功！");
    }

    /**
     * 宠托师或者店家
     * 确定客户预约信息
     */
    @ApiOperation(value = "确定客户预约信息")
    @PostMapping(value = "/confirm/{orderId}")
    @Transactional(rollbackFor = Exception.class)
    public GlobalResponse<String> confirm(
            @PathVariable Long orderId) {
        // 判断是否是自己的订单信息,如果不是则该用户没有权限确认订单信息
        Order order = orderService.lambdaQuery()
                .eq(Order::getPersonalServiceUserId, StpUtil.getLoginIdAsLong())
                .eq(Order::getId, orderId).one();
        if (order == null) {
            return GlobalResponse.failure("该订单信息已失效或者不是属于您的预约订单！");
        }
        // 判断该订单是否过期
        if (order.getState().equals(OrderState.CANCELED)) {
            return GlobalResponse.failure("该订单信息已过期！");
        }
        // 判断该订单是否已经被确定
        if (order.getState().equals(OrderState.SHIPPED)) {
            return GlobalResponse.failure("确定过的订单无法再次确定！");
        }
        // 判断该订单是否已经生成的二维码信息
        if (!StrUtil.isBlank(order.getQrcode())) {
            return GlobalResponse.failure("该订单已经确定过了！");
        }
        // 判断是否已经生成的二维码
        order.setState(OrderState.SHIPPED);
        // 生成唯一ID
        QrCode qrCode = new QrCode().setCreateTime(LocalDateTime.now())
                .setServiceUserId(order.getPersonalServiceUserId())
                .setExpiredTime(order.getReservationTime().plusHours(serviceConfig.getServiceExpiredTime()))
                .setData(order.getId().toString())
                .setIsUse(false);
        qrCodeMapper.insert(qrCode);

        // 支付成功之后生成二维码图片
        BufferedImage generate = QrCodeUtil.generate(qrCode.getId().toString(), 300, 300);

        // 写入到指定为止
        FileMap file = fileService.saveLocalFile(generate, "png");
        qrCode.setImgUrl(file.getUrl());
        qrCodeMapper.updateById(qrCode);

        order.setQrcode(file.getUrl());
        orderService.updateById(order);
        return GlobalResponse.success("确认客户预约成功！");
    }

    /**
     * 二维码扫描的接口，将扫描信息（Order）放入到这里面
     * 这个接口暂时不验证宠托师位置和其他信息
     * 只针对订单信息进行更新
     */
    @ApiOperation(value = "宠托师取消指定客户的预约信息")
    @PostMapping(value = "/scan")
    @Transactional(rollbackFor = Exception.class)
    public GlobalResponse<String> scan(@RequestParam Long qrcodeId) {
        QrCode qrCode = qrCodeMapper.selectById(qrcodeId);
        // 判断二维码是否过期
        if (LocalDateTime.now().isAfter(qrCode.getExpiredTime())) {
            return GlobalResponse.success("二维码已过期！");
        }
        // 判断二维码是否被使用
        if (qrCode.getIsUse()) {
            return GlobalResponse.success("二维码已经被使用！");
        }
        // 获取订单信息
        String orderId = qrCode.getData();
        Order order = orderService.getById(orderId);
        // 更新二维码状态
        qrCode.setIsUse(true);
        qrCodeMapper.updateById(qrCode);
        // 更新订单状态
        order.setState(OrderState.FINISHED);
        orderService.updateById(order);
        // 生成给宠托师的支付信息
        PayInfo payInfo = new PayInfo()
                .setUserId(StpUtil.getLoginIdAsLong())
                .setType(PayType.INCOME)
                .setPaymentOrder(order.getId())
                .setPayee(order.getUserId())
                .setAmount(order.getPrice());
        payService.save(payInfo);
        return GlobalResponse.success("二维码扫描成功，该订单已完成！");
    }

    /**
     * 取消指定客户的预约信息
     *
     * @param orderId    订单信息
     * @param shopRemark 取消备注
     * @return 返回信息
     */
    @ApiOperation(value = "宠托师取消指定客户的预约信息")
    @PostMapping(value = "/bookingCancel/{orderId}")
    @Transactional(rollbackFor = Exception.class)
    public GlobalResponse<String> bookingCancel(@PathVariable Long orderId,
                                                @RequestParam String shopRemark) {

        Order order = orderService.lambdaQuery()
                .eq(Order::getPersonalServiceUserId, StpUtil.getLoginIdAsLong())
                .eq(Order::getId, orderId).one();
        if (order == null) {
            return GlobalResponse.failure("没有该订单信息！");
        }
        // 判断该订单是否过期
        if (order.getState().equals(OrderState.CANCELED)) {
            return GlobalResponse.failure("该订单信息已过期！");
        }
        // 设置该订单状态为取消
        order.setState(OrderState.CANCELED_BY_MERCHANT)
                .setShopRemark(shopRemark);
        orderService.updateById(order);
        return GlobalResponse.success("取消客户预约成功！");
    }

    /**
     * 客户取消自己的预约信息
     *
     * @param orderId 订单信息
     * @return 返回预约取消成功的消息
     */
    @ApiOperation(value = "取消指定客户的预约信息")
    @PostMapping(value = "/cancel/{orderId}")
    @Transactional(rollbackFor = Exception.class)
    public GlobalResponse<String> cancel(@PathVariable Long orderId) {
        Order order = orderService.lambdaQuery()
                .eq(Order::getUserId, StpUtil.getLoginIdAsLong())
                .eq(Order::getId, orderId).one();
        if (order == null) {
            return GlobalResponse.failure("没有该订单信息！");
        }
        // 判断该订单是否过期
        if (order.getState().equals(OrderState.CANCELED)) {
            return GlobalResponse.failure("该订单信息已过期,或者已取消！");
        }
        // 设置该订单状态为取消
        order.setState(OrderState.CANCELED);
        orderService.updateById(order);
        return GlobalResponse.success("取消客户预约成功！");
    }


    /**
     * 查询指定用户所有的订单信息
     *
     * @param page 查询的参数信息
     */
    @ApiOperation(value = "查询指定用户所有的订单信息")
    @PostMapping("/query")
    public GlobalResponse<Map<String, Object>> orders(
            @RequestBody GlobalResponse<Order> page,
            @RequestParam String state) {
        // 判断是否要查询全部的数据
        boolean isAll = StrUtil.isBlank(state);
        // 这个接口只能查自己的订单信息
        GlobalResponse<Order> res = orderService.lambdaQuery()
                .eq(Order::getUserId, StpUtil.getLoginIdAsLong())
                .eq(!isAll, Order::getState,
                        isAll ? OrderState.WAIT_PAY : OrderState.valueOf(state))
                // .ge(isAll, Order::getState, OrderState.WAIT_PAY)
                // .le(isAll, Order::getState, OrderState.EVALUATED)
                .page(page);
        GlobalResponse<Map<String, Object>> response = new GlobalResponse<>();
        response.setTotal(res.getTotal())
                .setMessage(res.getMessage())
                .setCode(res.getCode())
                .setCurrent(res.getCurrent())
                .setPages(res.getPages())
                .setCodeStr(res.getCodeStr())
                .setSize(res.getSize());
        try {
            List<Map<String, Object>> list = res.getRecords()
                    .stream()
                    .map(orderService::orderInfo)
                    .toList();
            response.setRecords(list);
        } catch (Exception e) {
            log.info("订单信息查询失败 {}", e.getMessage());
        }
        return response;
    }

    /**
     * 获取目前宠托师所有的订单信息
     * @author 林河
     */
    @ApiOperation(value = "查询指定用户所有的订单信息")
    @PostMapping("/ptOrdersInfo")
    public GlobalResponse<Map<String, Object>> ptOrdersInfo() {
        List<Order> orders = orderService.lambdaQuery()
                .eq(Order::getPersonalServiceUserId, StpUtil.getLoginIdAsLong())
                .and(wrapper -> wrapper.eq(Order::getState, OrderState.PAYED)
                        .or()
                        .eq(Order::getState, OrderState.SHIPPED))
                .list();
        var maps = orders.stream().map(orderService::orderInfo).toList();
        return GlobalResponse.success("获取订阅用户信息成功！", maps);
    }

}
