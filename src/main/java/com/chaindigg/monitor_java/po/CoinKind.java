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
@TableName("coin_kind")
@ApiModel(value="CoinKind对象", description="")
public class CoinKind implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "币种管理表")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "主链名称")
    private String mainChain;

    @ApiModelProperty(value = "币种名称")
    private String coinName;

    @ApiModelProperty(value = "合约地址")
    private String contractAddr;

    @ApiModelProperty(value = "小数位")
    private Integer decimal;


}
