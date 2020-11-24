package com.chaindigg.monitor.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author chenghao
 * @since 2020-11-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user")
@ApiModel(value = "User对象", description = "")
public class User implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "用户表")
  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;

  @ApiModelProperty(value = "姓名")
  private String name;

  @ApiModelProperty(value = "电话")
  private String phone;

  @ApiModelProperty(value = "电子邮箱")
  private String email;

  @ApiModelProperty(value = "用户创建时间")
  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createTime;

  @ApiModelProperty(value = "用户信息修改时间")
  @TableField(fill = FieldFill.INSERT_UPDATE)
  private LocalDateTime updateTime;

  @ApiModelProperty(value = "访问权限")
  private Integer accessPrmision;

  @ApiModelProperty(value = "微服务appID")
  private Integer appId;

  @ApiModelProperty(value = "备注")
  private String remark;

  @ApiModelProperty(value = "用户状态")
  private Integer state;
}
