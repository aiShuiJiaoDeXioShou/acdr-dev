package com.yskj.acdr.master.user.entity;

import com.baomidou.mybatisplus.annotation.*;

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
@TableName("acdr_user_identity_verification")
@ApiModel(value = "UserIdentityVerification对象", description = "")
public class UserIdentityVerification implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户实名认证表主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("实名认证的用户id")
    @TableField(value = "user_id", fill = FieldFill.INSERT)
    private Long userId;

    @ApiModelProperty("实名认证接口返回的json数据")
    @TableField("json")
    private String json;

    @ApiModelProperty("真实姓名")
    @TableField("real_name")
    private String realName;

    @ApiModelProperty("真实性别")
    @TableField("real_sex")
    private String realSex;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("实名认证信息更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("身份证信息")
    @TableField("identity")
    private String identity;

    @ApiModelProperty("户籍")
    @TableField("domicile")
    private String domicile;

    @ApiModelProperty("出生年月日")
    @TableField("birthday")
    private String birthday;

    @ApiModelProperty("民族")
    @TableField("nationality")
    private String nationality;

    @ApiModelProperty("是否为复印件")
    @TableField("is_fake")
    private String isFake;
}
