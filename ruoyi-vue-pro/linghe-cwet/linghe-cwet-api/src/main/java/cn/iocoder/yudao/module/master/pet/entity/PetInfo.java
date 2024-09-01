package cn.iocoder.yudao.module.master.pet.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import cn.iocoder.yudao.module.common.verify.ValidAgeRange;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PutMapping;

import java.time.LocalDateTime;

/**
 * @author hjp
 * @since 2024-07-22
 */
@TableName(value = "acdr_pet_info")
@Getter
@Setter
@Accessors(chain = true)
public class PetInfo {

    /**
     * 宠物主键
     */
    @TableId(value = "`id`", type = IdType.ASSIGN_ID)
    @NotBlank(message = "宠物主键不能为空", groups = PutMapping.class)
    private String id;

    /**
     * 用户主键
     */
    @TableField(value = "`user_id`", fill = FieldFill.INSERT)
    private long userId;

    /**
     * 头像
     */
    @TableField(value = "`profile_url`")
    @Null(message = "头像不允许赋值")
    private String profileUrl;

    /**
     * 宠物姓名
     */
    @TableField(value = "`name`")
    @NotBlank(message = "宠物姓名不能为空")
    private String name;

    /**
     * 宠物年龄
     */
    @TableField(value = "`age`")
    @NotNull(message = "宠物年龄不能为空")
    @ValidAgeRange
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime age;

    /**
     * 宠物性别（0-女，1-男）
     */
    @TableField(value = "`sex`")
    @NotNull(message = "宠物性别不能为空")
    private Integer sex;

    /**
     * 宠物的爱好
     */
    @TableField(value = "`hobby`")
    private String hobby;

    /**
     * 宠物讨厌的东西
     */
    @TableField(value = "`detest`")
    private String detest;

    /**
     * 宠物的种类
     */
    @TableField(value = "`assort`")
    private Integer assort;

    /**
     * 品种
     */
    @TableField(value = "`breed`")
    @NotBlank(message = "品种不能为空")
    private String breed;

    /**
     * 是否绝育（0-未绝育，1-绝育）
     */
    @TableField(value = "`is_sterilization`")
    private Integer isSterilization;

    /**
     * 毛发颜色
     */
    @TableField(value = "`hair_color`")
    @NotNull(message = "毛发不能为空")
    private String hairColor;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
