package com.yskj.acdr.master.config.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
 * @since 2024-08-22
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("acdr_config")
@ApiModel(value = "Config对象")
public class Config implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("配置表key")
    @NotBlank(message = "key 不能为空")
    private String key;

    @ApiModelProperty("配置表value(有可能储存的是json数据 )")
    @TableField("value")
    @NotBlank(message = "value 不能为空")
    private String value;
}
