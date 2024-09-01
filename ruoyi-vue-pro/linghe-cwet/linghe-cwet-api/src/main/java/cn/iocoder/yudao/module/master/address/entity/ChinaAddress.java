package cn.iocoder.yudao.module.master.address.entity;

import cn.iocoder.yudao.module.common.verify.ValidPhone;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import jakarta.validation.constraints.NotBlank;

/**
 * <p>
 * 中国地址表
 * </p>
 *
 * @author 林河
 * @since 2024-08-03
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("acdr_china_address")
@ApiModel(value = "ChinaAddress对象", description = "中国地址表")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChinaAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("地址ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("省/直辖市/自治区/特别行政区")
    @TableField("province")
    @NotBlank(message = "省/直辖市/自治区/特别行政区不能为空")
    private String province;

    @ApiModelProperty("市/自治州/直辖市辖区")
    @TableField("city")
    @NotBlank(message = "市/自治州/直辖市辖区不能为空")
    private String city;

    @ApiModelProperty("区/县/自治县/市辖区")
    @TableField("district")
    @NotBlank(message = "区/县/自治县/市辖区不能为空")
    private String district;

    @ApiModelProperty("街道/乡/镇")
    @TableField("street")
    private String street;

    @ApiModelProperty("详细地址（门牌号等）")
    @TableField("detail_address")
    @NotBlank(message = "详细地址（门牌号等）不能为空")
    private String detailAddress;

    @ApiModelProperty("邮政编码")
    @TableField("postal_code")
    private String postalCode;

    @ApiModelProperty("地址手机号")
    @TableField(value = "phone")
    @ValidPhone
    @NotBlank(message = "手机号不能为空")
    private String phone;

    @ApiModelProperty("收货人姓名")
    @TableField(value = "name")
    @NotBlank(message = "收货人姓名")
    private String name;

    @ApiModelProperty("地址类型")
    @TableField(value = "type")
    private String type;

    @ApiModelProperty("自定义地址")
    @TableField(value = "custom_type")
    private String customType;

    @ApiModelProperty("用户ID")
    @TableField(value = "user_id", fill = FieldFill.INSERT)
    private Long userId;

    @ApiModelProperty("经度")
    @TableField(value = "latitude")
    private Long latitude;

    @ApiModelProperty("纬度")
    @TableField(value = "longitude")
    private Long longitude;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("是否为默认地址")
    @TableField("is_default")
    private Boolean isDefault;
}
