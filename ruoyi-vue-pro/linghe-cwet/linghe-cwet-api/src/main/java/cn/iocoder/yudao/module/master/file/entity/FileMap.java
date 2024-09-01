package cn.iocoder.yudao.module.master.file.entity;

import com.baomidou.mybatisplus.annotation.*;

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
 * @since 2024-08-14
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("acdr_file_map")
@ApiModel(value = "FileMap对象", description = "")
public class FileMap implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("映射id")
    @NotBlank(message = "映射id不能为空")
    @TableId(value = "`id`", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("被映射的图片路径")
    @NotBlank(message = "被映射的图片路径不能为空")
    @TableField("`url`")
    private String url;

    @ApiModelProperty("是否存在")
    @TableField("is_exist")
    private Boolean isExist;

    @ApiModelProperty("是否为本地文件")
    @TableField("is_local")
    private Boolean isLocal;

    @ApiModelProperty("在本地文件的情况下路径")
    @TableField("local_file_path")
    private String localFilePath;

    @ApiModelProperty("是否为web服务器文件")
    @TableField("is_web")
    private Boolean isWeb;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("上传用户")
    @TableField(value = "user_id")
    private long userId;
}
