package com.yskj.acdr.master.pet.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yskj.acdr.common.response.GlobalResponse;
import com.yskj.acdr.enums.PetSpecies;
import com.yskj.acdr.master.pet.entity.PetInfo;
import com.yskj.acdr.master.pet.mapper.PetInfoMapper;
import com.yskj.acdr.master.pet.service.PetInfoService;
import com.yskj.acdr.master.user.entity.Users;
import com.yskj.acdr.master.user.mapper.UsersMapper;
import com.yskj.acdr.utils.FileUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author hjp
 * @since 2024-07-22
 */
@Service
public class PetInfoServiceImpl extends ServiceImpl<PetInfoMapper, PetInfo>
        implements PetInfoService {
    @Value("${path.profile}")
    private String profile;
    @Resource
    private PetInfoMapper petInfoMapper;
    @Resource
    private UsersMapper wechatUserInfoMapper;

    @Override
    public GlobalResponse<PetInfo> addPet(MultipartFile file, PetInfo petInfo) {
        LambdaQueryWrapper<Users> userInfoWrapper = new LambdaQueryWrapper<>();
        long userID = StpUtil.getLoginIdAsLong();
        userInfoWrapper.eq(Users::getId, userID);
        Users userInfo = wechatUserInfoMapper.selectOne(userInfoWrapper);
        if (userInfo == null) {
            return GlobalResponse.failure("用户不存在");
        } else {
            // 默认只能添加六只宠物
            int petMaxNum = 6;
            Long l = petInfoMapper.selectCount(new LambdaQueryWrapper<PetInfo>().eq(PetInfo::getUserId, userID));
            if (l >= petMaxNum) {
                return GlobalResponse.failure("宠物数量已达上限，是否再添加一位您的爱宠");
            }
        }
        String petId = IdUtil.getSnowflakeNextIdStr();
        String flag = StpUtil.getLoginIdAsLong() + File.separator + petId;
        if (file != null) {
            String fileName = FileUtils.uploadSingleFile(file, profile, flag);
            petInfo.setId(petId).setProfileUrl("/profile/"+fileName);
        } else {
            //使用默认头像
            petInfo.setId(petId).setProfileUrl("default" + File.separator + "default-" + PetSpecies.getSortFromSortCode(petInfo.getAssort()) + ".jpg");
        }
        return petInfoMapper.insert(petInfo) > 0 ? GlobalResponse.success("添加成功") : GlobalResponse.failure("添加失败");
    }

    @Override
    public GlobalResponse<PetInfo> updatePet(MultipartFile file, PetInfo petInfo) {
        LambdaQueryWrapper<PetInfo> wrapper = new LambdaQueryWrapper<>();
        long userID = StpUtil.getLoginIdAsLong();
        wrapper.eq(PetInfo::getId, petInfo.getId()).eq(PetInfo::getUserId, userID);
        PetInfo one = petInfoMapper.selectOne(wrapper);
        if (one == null) {
            return GlobalResponse.failure("该宠物不存在");
        }
        if (file != null) {
            //先去删除旧头像
            if (!one.getProfileUrl().startsWith("default")) {
                String path = profile + one.getProfileUrl().trim().replace("/profile/", "");
                FileUtil.del(path);
            }
            String fileName = FileUtils.uploadSingleFile(file, profile, one.getUserId() + File.separator + one.getId());
            petInfo.setProfileUrl("/profile/"+fileName);
            return petInfoMapper.updateById(petInfo) > 0 ? GlobalResponse.success("修改成功") : GlobalResponse.failure("修改失败");
        } else {
            return petInfoMapper.updateById(petInfo) > 0 ? GlobalResponse.success("修改成功") : GlobalResponse.failure("修改失败");
        }
    }

}




