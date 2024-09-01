package cn.iocoder.yudao.module.master.pet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.iocoder.yudao.module.common.response.GlobalResponse;
import cn.iocoder.yudao.module.master.pet.entity.PetInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author hjp
 * @since 2024-07-22
 */
public interface PetInfoService extends IService<PetInfo> {

    GlobalResponse<PetInfo> addPet(MultipartFile file, PetInfo petInfo);

    GlobalResponse<PetInfo> updatePet(MultipartFile file, PetInfo petInfo);
}
