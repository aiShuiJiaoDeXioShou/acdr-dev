package cn.iocoder.yudao.module.master.personal.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * @author 林河
 * @since 2024-08-08
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("acdr_personal_service")
@ApiModel(value = "PersonalService对象", description = "")
public class PersonalService implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("上门服务主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Null(message = "上门服务主键不允许赋值", groups = PostMapping.class)
    @NotNull(message = "上门服务主键不允许为空", groups = PutMapping.class)
    private Long id;

    @ApiModelProperty("服务名称")
    @TableField("service_name")
    @NotBlank(message = "服务名称不能为空")
    @Size(min = 4, max = 30, message = "服务名称长度必须在4到20个字符之间")
    private String serviceName;

    @ApiModelProperty("服务封面图片")
    @TableField("url")
    @NotBlank(message = "服务封面图片不能为空")
    private String url;

    @ApiModelProperty("服务地点(范围)")
    @TableField("service_host")
    private String serviceHost;

    @ApiModelProperty("服务描述")
    @TableField("description")
    @NotBlank(message = "服务描述不能为空")
    @Size(min = 5, max = 1000, message = "服务描述必须在5到1000个字符之间")
    private String description;

    @ApiModelProperty("服务类型")
    @TableField("type")
    @NotBlank(message = "服务类型不能为空")
    private String type;

    @ApiModelProperty("服务状态")
    @TableField("state")
    @NotNull(message = "服务状态不能为空")
    private int state;

    @ApiModelProperty("服务价格(每小时多少钱)")
    @TableField("price")
    @NotNull(message = "服务价格(每小时多少钱)不能为空")
    @DecimalMin(value = "0.0", inclusive = false, message = "价格必须大于0")
    @DecimalMax(value = "1000.0", inclusive = false, message = "价格必须小于1000")
    private BigDecimal price;

    @ApiModelProperty("创建服务的用户")
    @TableField(value = "user_id", fill = FieldFill.INSERT)
    private Long userId;

    @ApiModelProperty("创建服务的时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新服务的时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("发布地址")
    @NotBlank(message = "发布地址不能为空")
    @TableField(value = "address")
    private String address;

    @ApiModelProperty("发布地址纬度")
    @NotBlank(message = "发布地址纬度不能为空")
    @TableField(value = "latitude")
    private String latitude;

    @ApiModelProperty("发布地址经度")
    @NotBlank(message = "发布地址经度不能为空")
    @TableField(value = "longitude")
    private String longitude;
}
