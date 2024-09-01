package com.yskj.acdr.master.pet.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yskj.acdr.common.response.GlobalResponse;
import com.yskj.acdr.master.pet.entity.PetExpertCertification;
import com.yskj.acdr.master.pet.mapper.PetExpertCertificationMapper;
import com.yskj.acdr.master.pet.service.PetExpertCertificationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yskj.acdr.master.pet.service.PetSpecialistCertificateService;
import com.yskj.acdr.master.pet.status.PetExpertCertificationStatus;
import com.yskj.acdr.master.user.service.AuthenticationService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 林河
 * @since 2024-08-13
 */
@Service
public class PetExpertCertificationServiceImpl extends ServiceImpl<PetExpertCertificationMapper, PetExpertCertification> implements PetExpertCertificationService {

    @Resource
    private AuthenticationService authService;

    @Resource
    private PetSpecialistCertificateService pscs;

    /**
     * @author 林河
     * 当前客户申请成为宠托师
     * @param pcf 申请成为宠托师的材料
     * @param urls 宠托师的材料图片
     */
    @Override
    public GlobalResponse<PetExpertCertification> apply(PetExpertCertification pcf) {
        // 判断当前用户是否实名认证
        if(!authService.isRealName()) {
            return GlobalResponse.failure("当前用户并未实名认证,请先实名认证！");
        }
        // 判断当前用户是否已经是宠托师
        if(pscs.hasCertificate()) {
            return GlobalResponse.failure("你已经是宠托师了，无需再次申请！");
        }
        // 判断当前用户是否已经提交材料
        PetExpertCertification pec = this.baseMapper.selectOne(new LambdaQueryWrapper<PetExpertCertification>()
                .eq(PetExpertCertification::getUserId, StpUtil.getLoginIdAsLong()));
        if(pec != null && pec.getState().equals(PetExpertCertificationStatus.PENDING)) {
            return GlobalResponse.failure("你已经提交过申请了，请耐心等待审核！", pec);
        }
        // 保存申请信息
        pcf.setUserId(StpUtil.getLoginIdAsLong())
                .setState(PetExpertCertificationStatus.PENDING);
        this.baseMapper.insert(pcf);
        return GlobalResponse.success("已成功提交材料，正在审核当中，请耐心等待!");
    }

}
