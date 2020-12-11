package com.chaindigg.monitor.userapi.controller;

import com.chaindigg.monitor.common.entity.MonitorTrans;
import com.chaindigg.monitor.common.enums.State;
import com.chaindigg.monitor.common.utils.ApiResponse;
import com.chaindigg.monitor.userapi.service.IMonitorTransService;
import com.chaindigg.monitor.userapi.service.IMonitorTransVOService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MonitorTransController {
  private final IMonitorTransVOService monitorTransVOService;
  private final IMonitorTransService monitorTransService;
  
  @GetMapping("/monitor-trans")
  public ApiResponse getMonitorTransListByUserId(String id, Integer currentPage, Integer pageSize) {
    try {
      return ApiResponse.create(
          State.SUCCESS, monitorTransVOService.selectByUserId(id, currentPage, pageSize));
    } catch (Exception e) {
      return ApiResponse.create(State.FAIL);
    }
  }
  
  @PostMapping("/monitor-trans")
  public ApiResponse getMonitorTransList(MonitorTrans monitorTrans) {
    try {
      return ApiResponse.create(State.SUCCESS, monitorTransService.add(monitorTrans));
    } catch (Exception e) {
      return ApiResponse.create(State.FAIL);
    }
  }
}
