package com.yskj.acdr.master.pet.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@TableName("acdr_pet_specialist_certificate")
@ApiModel(value = "PetSpecialistCertificate对象", description = "")
public class PetSpecialistCertificate implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("过期日期")
    @TableField("expired_time")
    private LocalDateTime expiredTime;

    @ApiModelProperty("资格证类型")
    @TableField("type")
    private String type;

    @ApiModelProperty("拥有者id")
    @TableField(value = "user_id", fill = FieldFill.INSERT)
    private Long userId;

    @ApiModelProperty("颁发者id")
    @TableField("admin_id")
    private Long adminId;

    @ApiModelProperty("该资格证下服务次数")
    @TableField("service_number")
    private Integer serviceNumber;

    @ApiModelProperty("资格证编号")
    @TableField("card_id")
    private Long cardId;
}
