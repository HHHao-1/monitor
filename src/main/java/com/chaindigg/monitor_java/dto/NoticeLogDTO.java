package com.chaindigg.monitor_java.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;


/**
 *
 * @author chenghao
 * @since 2020-11-17
 */

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class NoticeLogDTO implements Serializable {

  private static final long serialVersionUID = 3370441206108014043L;

  /**
   * 事件名称
   */
  private String eventName;
  /**
   * 币种id
   */
  private String coinKind;
  /**
   * 通知方式
   */
  private Integer noticeWay;
  /**
   * 用户名
   */
  private Integer userName;
  /**
   * 通知时间
   */
  private Date noticeTime;
}
