package cn.iocoder.yudao.module.master.community.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

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
@TableName("acdr_follows")
@ApiModel(value = "Follows对象", description = "")
public class Follows implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("follower_id")
    @NotNull(message = "关注者ID不能为空")
    private Long followerId;

    @TableField("following_id")
    @NotNull(message = "被关注者ID不能为空")
    private Long followingId;

    @TableField("created_time")
    private LocalDateTime createdTime;
}
