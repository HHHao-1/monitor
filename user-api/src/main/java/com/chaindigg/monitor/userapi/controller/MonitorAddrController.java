package com.chaindigg.monitor.userapi.controller;

import com.chaindigg.monitor.common.entity.MonitorAddr;
import com.chaindigg.monitor.common.enums.State;
import com.chaindigg.monitor.common.utils.ApiResponse;
import com.chaindigg.monitor.userapi.service.IMonitorAddrService;
import com.chaindigg.monitor.userapi.service.IMonitorAddrVOService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MonitorAddrController {
  private final IMonitorAddrVOService monitorAddrVOService;
  private final IMonitorAddrService monitorAddrService;

  @GetMapping("/monitor-addr")
  public ApiResponse getMonitorAddrListByUserId(String id, int currentPage, int pageSize) {
    try {
      return ApiResponse.create(
          State.SUCCESS, monitorAddrVOService.selectByUserId(id, currentPage, pageSize));
    } catch (Exception e) {
      return ApiResponse.create(State.FAIL);
    }
  }

  @PostMapping("/monitor-addr")
  public ApiResponse getMonitorAddrList(MonitorAddr monitorAddr) {
    try {
      return ApiResponse.create(State.SUCCESS, monitorAddrService.add(monitorAddr));
    } catch (Exception e) {
      return ApiResponse.create(State.FAIL);
    }
  }
}
