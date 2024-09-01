package com.yskj.acdr.master.pet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yskj.acdr.common.response.GlobalResponse;
import com.yskj.acdr.master.pet.entity.PetInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author hjp
 * @since 2024-07-22
 */
public interface PetInfoService extends IService<PetInfo> {

    GlobalResponse<PetInfo> addPet(MultipartFile file, PetInfo petInfo);

    GlobalResponse<PetInfo> updatePet(MultipartFile file, PetInfo petInfo);
}
