package com.yskj.acdr.master.pet.service;

import com.yskj.acdr.master.pet.entity.PetSpecialistCertificate;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 林河
 * @since 2024-08-13
 */
public interface PetSpecialistCertificateService extends IService<PetSpecialistCertificate> {
    /**
     * 判断当前用户是否拥有该资格证
     */
    boolean hasCertificate();

    /**
     * 获取当前用户的资格证
     */
    PetSpecialistCertificate getCertificate();

    /**
     * 获取指定用户的资格证
     */
    PetSpecialistCertificate getCertificate(Long userId);

}
