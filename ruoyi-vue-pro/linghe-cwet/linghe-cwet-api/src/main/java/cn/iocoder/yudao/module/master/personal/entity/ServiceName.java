package cn.iocoder.yudao.module.master.personal.entity;

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
 * @since 2024-08-25
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("acdr_service_name")
@ApiModel(value = "ServiceName对象", description = "")
public class ServiceName implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("宠托师服务id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("宠托师服务名称")
    @TableField("service_name")
    private String serviceName;

    @ApiModelProperty("宠托师图片路径")
    @TableField("image_url")
    private String imageUrl;

    @ApiModelProperty("优先级")
    @TableField("number")
    private Integer number;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
