package cn.iocoder.yudao.module.master.user.entity;

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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

/**
* <p>
*
* </p>
*
* @author 林河
* @since 2024-08-08
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("acdr_users")
@ApiModel(value = "UserBase对象", description = "")
    public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Null(message = "用户id不允许赋值")
    private Long id;

    @ApiModelProperty("用户名")
    @TableField("name")
    private String name;

    @ApiModelProperty("用户电话")
    @TableField("phone")
    private String phone;

    @ApiModelProperty("用户电子邮箱")
    @TableField("email")
    private String email;

    @ApiModelProperty("用户身份信息")
    @TableField("type_id")
    private String typeId;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @NotNull(message = "创建时间不能为空")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @NotNull(message = "更新时间不能为空")
    private LocalDateTime updateTime;

    @ApiModelProperty("用户头像")
    @TableField("avatar")
    private String avatar;

    @ApiModelProperty("用户昵称")
    @TableField("nickname")
    private String nickname;

    /**
     * 0-女；1-男
     */
    @TableField(value = "sex")
    private Integer sex;

    /**
     * 微信用户的唯一标识
     */
    @ApiModelProperty("微信用户的唯一标识")
    @TableField(value = "openid")
    private String openid;

    @ApiModelProperty("是否实名认证")
    @TableField(value = "is_real_name")
    private Boolean isRealName = false;

    @ApiModelProperty("是否拥有宠托师证书")
    @TableField(value = "is_pet_nursery")
    private Boolean isPetNursery = false;
}
