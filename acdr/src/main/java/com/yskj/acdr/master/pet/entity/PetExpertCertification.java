package com.yskj.acdr.master.pet.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yskj.acdr.master.pet.status.PetExpertCertificationStatus;
import com.yskj.acdr.serialization.handler.JsonTypeHandler;
import com.yskj.acdr.serialization.handler.ListStringTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author 林河
 * @since 2024-08-13
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("acdr_pet_expert_certification")
@ApiModel(value = "PetExpertCertification对象", description = "")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PetExpertCertification implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("宠物达人个人信息审核表")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("审核内容")
    @TableField("content")
    @NotBlank(message = "审核内容不能为空")
    private String content;

    @ApiModelProperty("审核材料")
    @TableField(value = "image_urls", typeHandler = JsonTypeHandler.class)
    private List<String> imageUrls;

    @ApiModelProperty("审核状态是否通过")
    @TableField("state")
    private PetExpertCertificationStatus state;

    @ApiModelProperty("审核管理员id")
    @TableField("verify_admin_id")
    private Long verifyAdminId;

    @ApiModelProperty("审核管理员id")
    @TableField("type")
    private String type;

    @ApiModelProperty("创建人")
    @TableField("user_id")
    private Long userId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
