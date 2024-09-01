package cn.iocoder.yudao.module.master.pet.controller;

import cn.iocoder.yudao.module.utils.LoginUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.module.common.response.GlobalResponse;
import cn.iocoder.yudao.module.master.file.service.FileMapService;
import cn.iocoder.yudao.module.master.pet.entity.PetExpertCertification;
import cn.iocoder.yudao.module.master.pet.entity.PetInfo;
import cn.iocoder.yudao.module.master.pet.entity.PetSpecialistCertificate;
import cn.iocoder.yudao.module.master.pet.service.PetExpertCertificationService;
import cn.iocoder.yudao.module.master.pet.service.PetInfoService;
import cn.iocoder.yudao.module.master.pet.service.PetSpecialistCertificateService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.groups.Default;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author hjp
 * @since 2024-07-22
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/pet/petInfo")
public class PetController {

    @Resource
    private PetInfoService petInfoService;

    @Resource
    private PetExpertCertificationService pecs;

    @Resource
    private PetSpecialistCertificateService pscs;

    @Resource
    private FileMapService fileService;

    /**
     * 账号添加宠物信息
     *
     * @param file    宠物头像
     * @param petInfo 宠物信息
     */
    @PostMapping("/addPet")
    public GlobalResponse<PetInfo> addPet(@RequestParam(value = "file", required = false) MultipartFile file,
                                          @Validated PetInfo petInfo) {
        return petInfoService.addPet(file, petInfo);
    }

    /**
     * 修改宠物信息
     *
     * @param file    宠物头像
     * @param petInfo 宠物信息
     */
    @PostMapping("/update")
    public GlobalResponse<PetInfo> update(@RequestParam(value = "file", required = false) MultipartFile file,
                                          @Validated({Default.class, PutMapping.class}) PetInfo petInfo) {
        return petInfoService.updatePet(file, petInfo);
    }

    /**
     * 删除宠物信息
     *
     * @param id 宠物id
     */
    @DeleteMapping("/delete")
    public GlobalResponse<String> delete(@NotBlank(message = "删除的宠物id不能为空") String id) {
        //先去删除头像
        PetInfo one = petInfoService.lambdaQuery().eq(PetInfo::getId, id).one();
        if (one == null) {
            return GlobalResponse.failure("该宠物不存在，无法删除失败");
        }
        if (one.getProfileUrl() != null) {
            if (!one.getProfileUrl().startsWith("default")) {
                FileUtil.del(one.getProfileUrl());
            }
        }
        return petInfoService.removeById(id) ? GlobalResponse.success("删除成功") : GlobalResponse.failure("删除失败");
    }

    /**
     * 查询用户下的所有的宠物信息
     *
     * @param page   分页对象
     * @param userId 用户主键
     */
    @GetMapping("/select")
    public GlobalResponse<PetInfo> select(GlobalResponse<PetInfo> page, @NotBlank(message = "用户主键不能为空") String userId) {
        return petInfoService.lambdaQuery().eq(PetInfo::getUserId, userId).page(page);
    }


    /**
     * 查询用户下的所有的宠物信息
     */
    @GetMapping("/my")
    public GlobalResponse<List<PetInfo>> my() {
        return GlobalResponse.success(
                petInfoService.lambdaQuery().eq(PetInfo::getUserId, LoginUtil.getLoginIdAsLong()).list()
        );
    }

    /**
     * 根据id获取该宠物信息
     */
    @GetMapping("/find_by_id/{id}")
    public GlobalResponse<PetInfo> find_by_id(@PathVariable Long id) {
        PetInfo one = petInfoService.lambdaQuery()
                .eq(PetInfo::getId, id)
                .eq(PetInfo::getUserId, LoginUtil.getLoginIdAsLong())
                .one();
        if (ObjectUtil.isEmpty(one)) {
            return GlobalResponse.failure("没有找到该宠物的信息");
        }
        return GlobalResponse.success(one);
    }

    /**
     * 当前客户申请成为宠托师
     *
     * @param pcf 申请成为宠托师的材料
     * @return GlobalResponse<PetExpertCertification>
     */
    @PostMapping("/apply")
    public GlobalResponse<PetExpertCertification> apply(
            @Validated @RequestBody PetExpertCertification pcf) {
        // 调用服务层的 apply 方法处理业务逻辑
        return pecs.apply(pcf);
    }

    /**
     * 获取当前用户宠托师信息
     * @return GlobalResponse<PetExpertCertification>
     */
    @GetMapping("/getExpertInfo")
    public GlobalResponse<PetSpecialistCertificate> getExpertInfo() {
        if (!pscs.hasCertificate()) {
            return GlobalResponse.failure("角色没有宠托师信息");
        }
        var one = pscs.lambdaQuery()
                    .eq(PetSpecialistCertificate::getUserId, LoginUtil.getLoginIdAsLong())
                    .one();
        return GlobalResponse.success(one);
    }
}

