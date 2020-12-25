package com.chaindigg.monitor.admin.controller;

import com.chaindigg.monitor.admin.service.INoticeLogService;
import com.chaindigg.monitor.common.enums.State;
import com.chaindigg.monitor.common.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
  
  @GetMapping("/notice-logs/addr")
  public ApiResponse getAddrNoticeLogs(String userName,
                                       String eventName, String coinKind, Integer currentPage, Integer pageSize) {
    try {
      return ApiResponse.create(
          State.SUCCESS,
          noticeLogService.selectAddrAll(userName, eventName, coinKind, currentPage, pageSize));
    } catch (Exception e) {
      e.printStackTrace();
      return ApiResponse.create(State.FAIL);
    }
  }
  
  @GetMapping("/notice-logs/trans")
  public ApiResponse getTransNoticeLogs(String userName, String coinKind, Integer currentPage, Integer pageSize) {
    try {
      return ApiResponse.create(
          State.SUCCESS, noticeLogService.selectTransAll(userName, coinKind, currentPage, pageSize));
    } catch (Exception e) {
      e.printStackTrace();
      return ApiResponse.create(State.FAIL);
    }
  }
  
  @GetMapping("/notice-logs")
  public ApiResponse getLogs(String userName,
                             String monitorType, String eventName, String coinKind, Integer currentPage, Integer pageSize) {
    try {
      return ApiResponse.create(
          State.SUCCESS,
          noticeLogService.selectAll(userName, monitorType, eventName, coinKind, currentPage, pageSize));
    } catch (Exception e) {
      e.printStackTrace();
      return ApiResponse.create(State.FAIL);
    }
  }
}
