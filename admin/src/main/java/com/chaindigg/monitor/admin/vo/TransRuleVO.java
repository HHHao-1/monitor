package com.chaindigg.monitor.admin.vo;

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
public class TransRuleVO implements Serializable {

  private static final long serialVersionUID = 1L;

  private Integer id;

  private String coinKind;

  private Integer userId;

  private LocalDateTime eventAddTime;

  private LocalDateTime eventUpdateTime;

  private Integer noticeWay;

  private String monitorMinVal;

  private Integer state;

  private String name;
}
