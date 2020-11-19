package com.chaindigg.monitor.vo;

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
public class TransRuleVO implements Serializable {

  private static final long serialVersionUID = 1L;

  private String coinKind;

  private Integer userId;

  private LocalDateTime eventAddTime;

  private LocalDateTime eventUpdateTime;

  private Integer noticeWay;

  private String monitorMinVal;

  private Integer state;

  private String name;

}
