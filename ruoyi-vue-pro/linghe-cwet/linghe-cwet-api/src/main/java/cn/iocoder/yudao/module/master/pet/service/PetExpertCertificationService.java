package cn.iocoder.yudao.module.master.pet.service;

import cn.iocoder.yudao.module.common.response.GlobalResponse;
import cn.iocoder.yudao.module.master.pet.entity.PetExpertCertification;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 林河
 * @since 2024-08-13
 */
public interface PetExpertCertificationService extends IService<PetExpertCertification> {

    /**
     * @author 林河
     * 当前客户申请成为宠托师
     * @param pcf 申请成为宠托师的材料
     * @param urls 宠托师的材料图片
     */
    GlobalResponse<PetExpertCertification> apply(PetExpertCertification pcf);
}
