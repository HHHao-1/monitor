package com.chaindigg.monitor_java.controller;


import com.chaindigg.monitor_java.dto.NoticeLogDTO;
import com.chaindigg.monitor_java.service.INoticeLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 服务控制器
 *
 * @author chenghao
 * @since 2020-11-16 17:40:50
 *
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class NoticeLogController {
  private final INoticeLogService noticeLogService;

  @GetMapping("/notice-logs/addresses")
  public List<NoticeLogDTO> getAddrNoticeLogList() {
    return noticeLogService.selectAddrAll();
  }

  @GetMapping("/notice-logs/transactions")
  public List<NoticeLogDTO> getTransNoticeLogList() {
    return noticeLogService.selectTransAll();
  }

  @GetMapping("/notice-logs")
  public Map<String, List<NoticeLogDTO>> getAll(){
    return noticeLogService.selectAll();
  }
}
