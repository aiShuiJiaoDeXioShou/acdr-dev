package com.yskj.acdr.master.personal.controller;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.yskj.acdr.common.response.GlobalResponse;
import com.yskj.acdr.master.personal.entity.PersonalService;
import com.yskj.acdr.master.personal.service.PersonalServiceService;
import com.yskj.acdr.master.pet.entity.PetInfo;
import com.yskj.acdr.master.pet.service.PetInfoService;
import com.yskj.acdr.master.user.entity.Users;
import com.yskj.acdr.master.user.service.UsersService;
import io.swagger.annotations.Api;
import jakarta.annotation.Resource;
import jakarta.validation.groups.Default;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 林河
 * @since 2024-08-08
 */
@Api(tags = "核心模块个人服务")
@RestController
@RequestMapping("/personal-service")
@Validated
@Slf4j
public class PersonalServiceController {

    @Resource
    private PersonalServiceService service;

    @Resource
    private UsersService usersService;

    @Resource
    private PetInfoService petInfoService;


    @PostMapping("/push")
    public GlobalResponse<Boolean> push(
            @RequestBody
            @Validated({PostMapping.class, Default.class})
            PersonalService ps) {

        return GlobalResponse.success(service.save(ps));
    }

    @PostMapping("/update")
    public GlobalResponse<Boolean> update(
            @RequestBody
            @Validated({PutMapping.class, Default.class})
            PersonalService ps) {
        return GlobalResponse.success(service.updateById(ps));
    }

    @PostMapping("/delete")
    public GlobalResponse<Boolean> delete(
            @RequestBody
            @Validated({DeleteMapping.class})
            PersonalService ps) {
        return GlobalResponse.success(service.removeById(ps));
    }

    @PostMapping("/get")
    public GlobalResponse<PersonalService> get(
            @RequestBody
            @Validated({GetMapping.class})
            PersonalService ps) {
        return GlobalResponse.success(service.getById(ps));
    }

    @PostMapping("/list")
    public GlobalResponse<PersonalService> list(
            @RequestBody GlobalResponse<PersonalService> page,
            @RequestParam(required = false) Double minLatitude,
            @RequestParam(required = false) Double maxLatitude,
            @RequestParam(required = false) Double minLongitude,
            @RequestParam(required = false) Double maxLongitude,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) String sortOrder) {

        PersonalService condition = page.getData(); // 获取查询条件

        LambdaQueryChainWrapper<PersonalService> queryWrapper = service.lambdaQuery();

        queryWrapper.eq(condition.getId() != null, PersonalService::getId, condition.getId())
                .like(StringUtils.isNotBlank(condition.getServiceName()), PersonalService::getServiceName, condition.getServiceName())
                .eq(StringUtils.isNotBlank(condition.getUrl()), PersonalService::getUrl, condition.getUrl())
                .like(StringUtils.isNotBlank(condition.getServiceHost()), PersonalService::getServiceHost, condition.getServiceHost())
                .like(StringUtils.isNotBlank(condition.getDescription()), PersonalService::getDescription, condition.getDescription())
                .eq(StringUtils.isNotBlank(condition.getType()), PersonalService::getType, condition.getType())
                .eq(condition.getState() != 0, PersonalService::getState, condition.getState())
                .ge(condition.getPrice() != null, PersonalService::getPrice, condition.getPrice())
                .eq(condition.getUserId() != null, PersonalService::getUserId, condition.getUserId())
                .ge(condition.getCreateTime() != null, PersonalService::getCreateTime, condition.getCreateTime())
                .le(condition.getUpdateTime() != null, PersonalService::getUpdateTime, condition.getUpdateTime())
                .like(StringUtils.isNotBlank(condition.getAddress()), PersonalService::getAddress, condition.getAddress());

        // 添加经度和纬度范围的条件
        if (minLatitude != null && maxLatitude != null) {
            queryWrapper.between(PersonalService::getLatitude, minLatitude, maxLatitude);
        }
        if (minLongitude != null && maxLongitude != null) {
            queryWrapper.between(PersonalService::getLongitude, minLongitude, maxLongitude);
        }

        // 根据sortField和sortOrder进行排序
        if ("price".equals(sortField)) {
            queryWrapper.orderBy(true, "asc".equals(sortOrder), PersonalService::getPrice);
        } else if ("createTime".equals(sortField)) {
            queryWrapper.orderBy(true, "desc".equals(sortOrder), PersonalService::getCreateTime);
        }

        // 分页查询
        GlobalResponse<PersonalService> resultPage = queryWrapper.page(page);

        return resultPage;
    }


    /**
     * @param id 查询服务id
     * @author linghe
     * 展示宠物个人服务详细信息
     */
    @GetMapping("/service/{id}")
    public GlobalResponse<Map<String, Object>> personalDetail(@PathVariable Long id) {
        PersonalService p = service.getById(id);
        Long userId = p.getUserId();
        Users user = usersService.getById(userId);

        // 获取指定用户的宠物信息
        List<PetInfo> list = petInfoService.lambdaQuery()
                .eq(PetInfo::getUserId, userId)
                .list();

        // 获取宠物信息
        var pets = list.stream()
                .map(petInfo -> Map.<String, Object>of("name", petInfo.getName(),
                        "id", petInfo.getId(),
                        "sex", petInfo.getSex(),
                        "profileUrl", petInfo.getProfileUrl()))
                .toList();

        // 将这些数据都打散装配到Map当中
        var map = MapUtil.<String, Object>builder()
                .put("serviceId", p.getId())
                .put("serviceName", p.getServiceName())
                .put("bgUrl", p.getUrl())
                .put("description", p.getDescription())
                .put("serviceType", p.getType())
                .put("latitude", p.getLatitude())
                .put("longitude", p.getLongitude())
                .put("address", p.getAddress())
                .put("serviceState", p.getState())
                .put("createTime", p.getCreateTime())
                .put("updateTime", p.getUpdateTime())
                .put("price", p.getPrice())
                .put("serviceUserId", p.getUserId()) // 服务者ID
                .put("userName", user.getName())
                .put("userAvatar", user.getAvatar())
                .put("pets", pets)
                .map();

        return GlobalResponse.success(map);
    }


}
