package com.chaindigg.monitor.controller;


import com.chaindigg.monitor.utils.ApiResponse;
import com.chaindigg.monitor.vo.NoticeLogVO;
import com.chaindigg.monitor.service.INoticeLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
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
  public ApiResponse<NoticeLogVO> getAddrNoticeLogs(String eventName, String coinKind, int currentPage, int pageSize) {
    ApiResponse apiResponse = new ApiResponse();
    try {
      return apiResponse.success(noticeLogService.selectAddrAll(eventName, coinKind, currentPage, pageSize));
    } catch (Exception e) {
      e.printStackTrace();
      return apiResponse.fail();
    }
  }



  @GetMapping("/notice-logs/transactions")
  public ApiResponse<NoticeLogVO> getTransNoticeLogs(String coinKind, int currentPage, int pageSize) {
    ApiResponse apiResponse = new ApiResponse();
    try {
      return apiResponse.success(noticeLogService.selectTransAll(coinKind, currentPage, pageSize));
    } catch (Exception e) {
      e.printStackTrace();
      return apiResponse.fail();
    }
  }

  @GetMapping("/notice-logs")
  public ApiResponse<NoticeLogVO> getLogs(@Nullable String monitorType, @Nullable String eventName, @Nullable String coinKind, int currentPage, int pageSize) {
    ApiResponse apiResponse = new ApiResponse();
    try {
      return apiResponse.success(noticeLogService.selectAll(monitorType, eventName, coinKind, currentPage, pageSize));
    } catch (Exception e) {
      e.printStackTrace();
      return apiResponse.fail();
    }
  }
}
