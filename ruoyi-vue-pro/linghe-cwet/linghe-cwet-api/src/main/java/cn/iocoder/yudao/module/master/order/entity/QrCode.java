package cn.iocoder.yudao.module.master.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("acdr_qr_code")
public class QrCode {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty("二维码id")
    private Long id;

    @ApiModelProperty("二维码创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @ApiModelProperty("二维码过期时间")
    @TableField("expired_time")
    private LocalDateTime expiredTime;

    @ApiModelProperty("二维码服务对象的ID")
    @TableField("service_user_id")
    private Long serviceUserId;

    @ApiModelProperty("二维码是否被使用")
    @TableField("is_use")
    private Boolean isUse;

    @ApiModelProperty("二维码生成的图片路径")
    @TableField("img_url")
    private String imgUrl;

    @ApiModelProperty("二维码扫描之后的数据")
    @TableField("data")
    private String data;

}
