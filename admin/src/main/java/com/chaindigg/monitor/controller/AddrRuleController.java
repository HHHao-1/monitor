package com.chaindigg.monitor.controller;

import com.chaindigg.monitor.enums.State;
import com.chaindigg.monitor.service.IAddrRuleVOService;
import com.chaindigg.monitor.utils.ApiResponse;
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
public class AddrRuleController {
  private final IAddrRuleVOService addrRuleService;

  @GetMapping("/addr-rules")
  public ApiResponse getAllRulesList(
      String event, String userName, String userId, int currentPage, int pageSize) {
    try {
      return ApiResponse.create(
          State.SUCCESS, addrRuleService.selectAll(event, userName, userId, currentPage, pageSize));
    } catch (Exception e) {
      e.printStackTrace();
      return ApiResponse.create(State.FAIL);
    }
  }
}
