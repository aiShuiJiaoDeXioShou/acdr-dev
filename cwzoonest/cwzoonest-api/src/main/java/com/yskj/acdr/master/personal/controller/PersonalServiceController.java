package com.yskj.acdr.master.personal.controller;

import cn.hutool.core.map.MapUtil;
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

import java.io.Serializable;
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
            @RequestBody
            @Validated({GetMapping.class})
            GlobalResponse<PersonalService> page) {
        return service.lambdaQuery().page(page);
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
                .put("userName", user.getName())
                .put("userAvatar", user.getAvatar())
                .put("pets", pets)
                .map();

        return GlobalResponse.success(map);
    }


}
