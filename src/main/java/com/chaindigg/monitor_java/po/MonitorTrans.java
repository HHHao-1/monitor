package com.chaindigg.monitor_java.po;

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
 * <p>
 * 
 * </p>
 *
 * @author chenghao
 * @since 2020-11-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("monitor_trans")
@ApiModel(value="MonitorTrans对象", description="")
public class MonitorTrans implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "交易监控表")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "交易哈希")
    private String transHash;

    @ApiModelProperty(value = "交易发送地址")
    private String fromAddress;

    @ApiModelProperty(value = "交易接收地址")
    private String toAddress;

    @ApiModelProperty(value = "异动额度")
    private String unusualCount;

    @ApiModelProperty(value = "异动时间")
    private LocalDateTime unusualTime;

    @ApiModelProperty(value = "通知时间")
    private LocalDateTime noticeTime;

    @ApiModelProperty(value = "交易监控规则id")
    private Integer transRuleId;


}
