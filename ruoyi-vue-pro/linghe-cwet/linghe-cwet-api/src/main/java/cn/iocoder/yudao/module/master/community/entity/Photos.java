package cn.iocoder.yudao.module.master.community.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@TableName("acdr_photos")
@ApiModel(value = "Photos对象", description = "照片实体")
public class Photos implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @NotNull(message = "帖子ID不能为空")
    @TableField("post_id")
    private Long postId;

    @NotBlank(message = "图片URL不能为空")
    @Size(max = 255, message = "图片URL不能超过255个字符")
    @TableField("photo_url")
    private String photoUrl;

    @TableField("created_time")
    private LocalDateTime createdTime;
}

