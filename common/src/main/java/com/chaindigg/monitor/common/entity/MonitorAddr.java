package com.chaindigg.monitor.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("monitor_addr")
@ApiModel(value = "MonitorAddr对象", description = "")
public class MonitorAddr implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "地址异动监控表")
  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;

  @ApiModelProperty(value = "交易哈希")
  private String transHash;

  @ApiModelProperty(value = "异动额度")
  private String unusualCount;

  @ApiModelProperty(value = "异动时间")
  private LocalDateTime unusualTime;

  @ApiModelProperty(value = "通知时间")
  private LocalDateTime noticeTime;

  @ApiModelProperty(value = "地址监控规则id")
  private Integer addrRuleId;
}
