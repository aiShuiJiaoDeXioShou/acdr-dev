package com.yskj.acdr.master.pet.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yskj.acdr.master.pet.entity.PetSpecialistCertificate;
import com.yskj.acdr.master.pet.mapper.PetSpecialistCertificateMapper;
import com.yskj.acdr.master.pet.service.PetSpecialistCertificateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class PetSpecialistCertificateServiceImpl extends ServiceImpl<PetSpecialistCertificateMapper, PetSpecialistCertificate> implements PetSpecialistCertificateService {

    /**
     * 判断当前用户是否拥有该资格证
     */
    @Override
    public boolean hasCertificate() {
        return getCertificate() != null;
    }

    /**
     * 获取当前用户的资格证
     */
    @Override
    public PetSpecialistCertificate getCertificate() {
        return getCertificate(StpUtil.getLoginIdAsLong());
    }

    /**
     * 获取指定用户的资格证
     */
    @Override
    public PetSpecialistCertificate getCertificate(Long userId) {
        PetSpecialistCertificateMapper mapper = this.baseMapper;
        LambdaQueryWrapper<PetSpecialistCertificate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PetSpecialistCertificate::getUserId, userId);
        PetSpecialistCertificate sc = mapper.selectOne(queryWrapper);
        return sc;
    }

}
