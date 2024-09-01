package cn.iocoder.yudao.module.master.notifications.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

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
 * @since 2024-08-12
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("acdr_notifications")
@ApiModel(value = "Notifications对象", description = "")
public class Notifications implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("消息主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("内容")
    @NotBlank(message = "内容不能为空！")
    @TableField("content")
    private String content;

    @ApiModelProperty("需要跳转的页面")
    @TableField("url")
    private String url;

    @ApiModelProperty("消息创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("消息创建状态")
    @TableField("status")
    private Integer status;

    @ApiModelProperty("消息类型")
    @TableField("type")
    private String type;

    @ApiModelProperty("消息创建者")
    @TableField(value = "user_id", fill = FieldFill.INSERT)
    private Long userId;

    @ApiModelProperty("消息接受范围")
    @TableField("receiver_scope")
    private String receiverScope;

    @ApiModelProperty("消息更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
