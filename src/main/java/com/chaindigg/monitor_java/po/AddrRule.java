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
@TableName("addr_rule")
@ApiModel(value="AddrRule对象", description="")
public class AddrRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "地址监控规则表")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "事件名称")
    private String eventName;

    @ApiModelProperty(value = "币种")
    private String coinKind;

    @ApiModelProperty(value = "地址信息")
    private String address;

    @ApiModelProperty(value = "事件添加时间")
    private LocalDateTime eventAddTime;

    @ApiModelProperty(value = "事件修改时间")
    private LocalDateTime eventUpdateTime;

    @ApiModelProperty(value = "通知方式")
    private Integer noticeWay;

    @ApiModelProperty(value = "监控阈值")
    private String monitorMinVal;

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "地址监控规则状态")
    private Integer state;

    @ApiModelProperty(value = "地址标注")
    private String addressMark;


}
