package com.yskj.acdr.master.community.entity;

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
@TableName("acdr_posts")
@ApiModel(value = "Posts对象", description = "帖子实体")
public class Posts implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @NotNull(message = "用户ID不能为空")
    @TableField(value = "user_id", fill = FieldFill.INSERT)
    private Long userId;

    @NotBlank(message = "帖子标题不能为空")
    @Size(max = 255, message = "帖子标题不能超过255个字符")
    @TableField("title")
    private String title;

    @NotBlank(message = "帖子内容不能为空")
    @TableField("content")
    private String content;

    @TableField("type")
    private String type;

    @TableField("location")
    private String location;

    @NotNull(message = "是否公开可见不能为空")
    @TableField("is_public")
    private Boolean isPublic;

    @TableField("created_time")
    private LocalDateTime createdTime;

    @TableField("updated_time")
    private LocalDateTime updatedTime;

    @TableField("deleted_at")
    private LocalDateTime deletedAt;
}

