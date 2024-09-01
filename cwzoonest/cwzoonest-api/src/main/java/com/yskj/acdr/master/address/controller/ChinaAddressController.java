package com.yskj.acdr.master.address.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yskj.acdr.master.address.entity.ChinaAddress;
import com.yskj.acdr.master.address.mapper.ChinaAddressMapper;
import com.yskj.acdr.master.address.service.IChinaAddressService;
import com.yskj.acdr.common.response.GlobalResponse;
import com.yskj.acdr.master.pet.entity.PetInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 中国地址表 前端控制器
 * </p>
 *
 * @author 林河
 * @since 2024-08-03
 */
@Api(tags = "中国地址表")
@RestController
@RequestMapping("/china-address")
public class ChinaAddressController {

    @Resource
    private IChinaAddressService service;

    @Resource
    private ChinaAddressMapper chinaAddressMapper;

    @Value("${map.amapkey}")
    private String amapkey;

    @Value("${map.url_geo}")
    private String urlGeo;

    @ApiOperation(value = "中国地址表分页列表", response = ChinaAddress.class)
    @PostMapping(value = "/page")
    public GlobalResponse<ChinaAddress> list(GlobalResponse<ChinaAddress> page) {
        return service.lambdaQuery().page(page);
    }

    /**
     * 查询用户下的所有的地址信息
     */
    @GetMapping("/my")
    public GlobalResponse<List<ChinaAddress>> my() {
        return GlobalResponse.success(
                service.lambdaQuery().eq(ChinaAddress::getUserId, StpUtil.getLoginIdAsLong()).list()
        );
    }

    @ApiOperation(value = "中国地址表详情", response = ChinaAddress.class)
    @GetMapping(value = "/info/{id}")
    public GlobalResponse<ChinaAddress> info(@PathVariable Long id) {
        ChinaAddress chinaAddress = service.getById(id);
        return GlobalResponse.success(chinaAddress);
    }

    @ApiOperation(value = "中国地址表新增")
    @PostMapping(value = "/add")
    public GlobalResponse<ChinaAddress> add(@Validated @RequestBody ChinaAddress param) {
        String address = param.getProvince() + param.getCity() + param.getDetailAddress();
        String res = HttpUtil.get(urlGeo+"?key=%s&address=%s".formatted(amapkey, address));
        // 解析res json 数据
        JSONObject resMap = JSONUtil.parseObj(res);
        if(resMap.isEmpty()) {
            return GlobalResponse.failure("服务错误，请联系管理人员修复！");
        }
        if(resMap.get("status", Integer.class) == 1) {
            // 获取经纬度信息
            String[] locations = resMap.get("location", String.class).split(",");
            param.setLongitude(Long.parseLong(locations[0]));
            param.setLatitude(Long.parseLong(locations[1]));
        } else {
            return GlobalResponse.failure("错误地址信息无法添加，"+resMap.get("info")+"!");
        }
        service.save(param);
        return GlobalResponse.success("中国地址表新增成功！");
    }

    @ApiOperation(value = "中国地址表修改")
    @PostMapping(value = "/modify")
    public GlobalResponse<ChinaAddress> modify(@RequestBody @Validated ChinaAddress param) {
        service.updateById(param);
        return GlobalResponse.success("中国地址表修改成功！");
    }

    @ApiOperation(value = "中国地址表删除(单个条目)")
    @GetMapping(value = "/remove/{id}")
    public GlobalResponse<ChinaAddress> remove(@PathVariable Long id) {
        service.removeById(id);
        return GlobalResponse.success("中国地址表删除(单个条目)！");
    }

    @ApiOperation(value = "中国地址表删除(多个条目)")
    @PostMapping(value = "/removes")
    public GlobalResponse<ChinaAddress> removes(@RequestBody List<Long> ids) {
        service.removeBatchByIds(ids);
        return GlobalResponse.success("中国地址表删除(多个条目)！");
    }
}
