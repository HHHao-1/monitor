package com.chaindigg.monitor.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;


/**
 * @author chenghao
 * @since 2020-11-17
 */

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class MonitorAddrVO implements Serializable {

  private static final long serialVersionUID = -7306681591763889736L;
  /**
   * 事件名称
   */
  private String eventName;
  /**
   * 币种
   */
  private String coinKind;
  /**
   * 地址信息
   */
  private String address;
  /**
   * 地址标注
   */
  private String addressMark;
  /**
   * 交易哈希
   */
  private String transHash;
  /**
   * 异动额度
   */
  private String unusualCount;
  /**
   * 异动时间
   */
  private Date unusualTime;
}
