package cn.iocoder.yudao.module.master.community.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
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
@TableName("acdr_favorites")
@ApiModel(value = "Favorites对象", description = "")
public class Favorites implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "user_id", fill = FieldFill.INSERT)
    private Long userId;

    @TableField("post_id")
    private Long postId;

    @TableField("created_time")
    private LocalDateTime createdTime;
}
