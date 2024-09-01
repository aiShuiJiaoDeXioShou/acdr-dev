package com.yskj.acdr.master.mall.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yskj.acdr.common.response.GlobalResponse;
import com.yskj.acdr.master.mall.entity.*;
import com.yskj.acdr.master.mall.mapper.*;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/mall")
public class MallController {

    private static final Log log = LogFactory.get();

    @Autowired
    private StoresMapper storeMapper;

    @Autowired
    private ProductsMapper productMapper;

    @Autowired
    private OrdersMapper orderMapper;

    @Autowired
    private CartItemsMapper cartItemMapper;

    /**
     * 创建商店
     * @param store 商店实体
     * @return 创建结果
     */
    @PostMapping("/stores")
    public GlobalResponse<Stores> createStores(@RequestBody Stores store) {
        try {
            store.setCreatedTime(LocalDateTime.now());
            store.setUpdatedTime(LocalDateTime.now());
            storeMapper.insert(store);
            return GlobalResponse.success(store);
        } catch (Exception e) {
            log.error("创建商店失败: {}", e.getMessage());
            return GlobalResponse.failure("创建商店失败，请联系管理员");
        }
    }

    /**
     * 获取商店列表
     * @return 商店列表
     */
    @GetMapping("/stores")
    public GlobalResponse<List<Stores>> getStoress() {
        try {
            List<Stores> stores = storeMapper.selectList(new QueryWrapper<>());
            return GlobalResponse.success(stores);
        } catch (Exception e) {
            log.error("获取商店列表失败: {}", e.getMessage());
            return GlobalResponse.failure("获取商店列表失败，请联系管理员");
        }
    }

    /**
     * 添加商品
     * @param product 商品实体
     * @return 添加结果
     */
    @PostMapping("/products")
    public GlobalResponse<Products> addProducts(@RequestBody Products product) {
        try {
            product.setCreatedTime(LocalDateTime.now());
            product.setUpdatedTime(LocalDateTime.now());
            productMapper.insert(product);
            return GlobalResponse.success(product);
        } catch (Exception e) {
            log.error("添加商品失败: {}", e.getMessage());
            return GlobalResponse.failure("添加商品失败，请联系管理员");
        }
    }

    /**
     * 获取商店的商品列表
     * @param storeId 商店ID
     * @return 商品列表
     */
    @GetMapping("/stores/{storeId}/products")
    public GlobalResponse<List<Products>> getProductssByStores(@PathVariable Long storeId) {
        try {
            List<Products> products = productMapper.selectList(new QueryWrapper<Products>().eq("store_id", storeId));
            return GlobalResponse.success(products);
        } catch (Exception e) {
            log.error("获取商品列表失败: {}", e.getMessage());
            return GlobalResponse.failure("获取商品列表失败，请联系管理员");
        }
    }

    /**
     * 创建订单
     * @param order 订单实体
     * @return 创建结果
     */
    @PostMapping("/orders")
    @Transactional(rollbackFor = Exception.class)
    public GlobalResponse<Orders> createOrders(@RequestBody Orders order) {
        try {
            order.setCreatedTime(LocalDateTime.now());
            order.setUpdatedTime(LocalDateTime.now());
            orderMapper.insert(order);
            return GlobalResponse.success(order);
        } catch (Exception e) {
            log.error("创建订单失败: {}", e.getMessage());
            return GlobalResponse.failure("创建订单失败，请联系管理员");
        }
    }

    /**
     * 获取订单详情
     * @param orderId 订单ID
     * @return 订单详情
     */
    @GetMapping("/orders/{orderId}")
    public GlobalResponse<Orders> getOrdersDetails(@PathVariable Long orderId) {
        try {
            Orders order = orderMapper.selectById(orderId);
            if (order == null) {
                return GlobalResponse.failure("订单不存在");
            }
            return GlobalResponse.success(order);
        } catch (Exception e) {
            log.error("获取订单详情失败: {}", e.getMessage());
            return GlobalResponse.failure("获取订单详情失败，请联系管理员");
        }
    }


    /**
     * 添加商品到购物车
     * @param cartItem 购物车实体
     * @return 添加结果
     */
    @PostMapping("/cart")
    public GlobalResponse<CartItems> addToCart(@RequestBody CartItems cartItem) {
        try {
            cartItem.setCreatedTime(LocalDateTime.now());
            cartItem.setUpdatedTime(LocalDateTime.now());
            cartItemMapper.insert(cartItem);
            return GlobalResponse.success(cartItem);
        } catch (Exception e) {
            log.error("添加商品到购物车失败: {}", e.getMessage());
            return GlobalResponse.failure("添加商品到购物车失败，请联系管理员");
        }
    }

    /**
     * 获取用户的购物车内容
     * @return 购物车列表
     */
    @GetMapping("/cart")
    public GlobalResponse<List<CartItems>> getCartItemss() {
        try {
            Long userId = StpUtil.getLoginIdAsLong();
            List<CartItems> cartItems = cartItemMapper.selectList(new QueryWrapper<CartItems>().eq("user_id", userId));
            return GlobalResponse.success(cartItems);
        } catch (Exception e) {
            log.error("获取购物车内容失败: {}", e.getMessage());
            return GlobalResponse.failure("获取购物车内容失败，请联系管理员");
        }
    }
}

