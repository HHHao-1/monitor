package com.chaindigg.monitor.admin.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class AddrRuleVO implements Serializable {
  private static final long serialVersionUID = 1L;

  private Integer id;

  private String eventName;

  private String coinKind;

  private String address;

  private LocalDateTime eventAddTime;

  private LocalDateTime eventUpdateTime;

  private Integer noticeWay;

  private String monitorMinVal;

  private Integer userId;

  private Integer state;

  private String addressMark;

  private String name;
}
