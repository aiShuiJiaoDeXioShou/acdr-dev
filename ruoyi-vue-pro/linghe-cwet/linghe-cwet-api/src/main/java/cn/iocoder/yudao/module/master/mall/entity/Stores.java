package cn.iocoder.yudao.module.master.mall.entity;

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
 * @since 2024-08-12
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("acdr_stores")
@ApiModel(value = "Stores对象", description = "")
public class Stores implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("type_id")
    private Long typeId;

    @TableField("name")
    private String name;

    @TableField("description")
    private String description;

    @TableField("location")
    private String location;

    @TableField("owner_id")
    private Long ownerId;

    @TableField("created_time")
    private LocalDateTime createdTime;

    @TableField("updated_time")
    private LocalDateTime updatedTime;
}
