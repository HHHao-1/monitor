package com.chaindigg.monitor.controller;


import com.chaindigg.monitor.vo.NoticeLogVO;
import com.chaindigg.monitor.service.INoticeLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 服务控制器
 *
 * @author chenghao
 * @since 2020-11-16 17:40:50
 */

@RequiredArgsConstructor
@RestController
public class NoticeLogController {
  private final INoticeLogService noticeLogService;

  @GetMapping("/notice-logs/addresses")
  public List<NoticeLogVO> getAddrNoticeLogList(int currentPage, int pageSize) {
    return noticeLogService.selectAddrAll(currentPage, pageSize);
  }

  @GetMapping("/notice-logs/transactions")
  public List<NoticeLogVO> getTransNoticeLogList(int currentPage, int pageSize) {
    return noticeLogService.selectTransAll(currentPage, pageSize);
  }

  @GetMapping("/notice-logs")
  public List<NoticeLogVO> getAll(int currentPage, int pageSize) {
    return noticeLogService.selectAll(currentPage, pageSize);
  }
}
