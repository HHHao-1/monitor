package com.chaindigg.monitor.userapi.vo;

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
public class MonitorTransVO implements Serializable {

  private static final long serialVersionUID = 2400401206168580693L;
  /** 币种 */
  private String coinKind;
  /** 交易哈希 */
  private String transHash;
  /** 交易发送地址 */
  private String fromAddress;
  /** 交易接收地址 */
  private String toAddress;
  /** 异动额度 */
  private String unusualCount;
  /** 异动时间 */
  private Date unusualTime;
}
