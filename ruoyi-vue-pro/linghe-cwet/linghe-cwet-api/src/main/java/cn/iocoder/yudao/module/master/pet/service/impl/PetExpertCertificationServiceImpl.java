package cn.iocoder.yudao.module.master.pet.service.impl;

import cn.iocoder.yudao.module.utils.LoginUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import cn.iocoder.yudao.module.common.response.GlobalResponse;
import cn.iocoder.yudao.module.master.pet.entity.PetExpertCertification;
import cn.iocoder.yudao.module.master.pet.mapper.PetExpertCertificationMapper;
import cn.iocoder.yudao.module.master.pet.service.PetExpertCertificationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.iocoder.yudao.module.master.pet.service.PetSpecialistCertificateService;
import cn.iocoder.yudao.module.master.pet.status.PetExpertCertificationStatus;
import cn.iocoder.yudao.module.master.user.service.AuthenticationService;
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
                .eq(PetExpertCertification::getUserId, LoginUtil.getLoginIdAsLong()));
        if(pec != null && pec.getState().equals(PetExpertCertificationStatus.PENDING)) {
            return GlobalResponse.failure("你已经提交过申请了，请耐心等待审核！", pec);
        }
        // 保存申请信息
        pcf.setUserId(LoginUtil.getLoginIdAsLong())
                .setState(PetExpertCertificationStatus.PENDING);
        this.baseMapper.insert(pcf);
        return GlobalResponse.success("已成功提交材料，正在审核当中，请耐心等待!");
    }

}
