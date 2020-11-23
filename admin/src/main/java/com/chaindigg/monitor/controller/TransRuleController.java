package com.chaindigg.monitor.controller;

import com.chaindigg.monitor.enums.State;
import com.chaindigg.monitor.service.ITransRuleVOService;
import com.chaindigg.monitor.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 服务控制器
 *
 * @author chenghao
 * @since 2020-11-16 17:40:50
 */
@RequiredArgsConstructor
@RestController
public class TransRuleController {
  private final ITransRuleVOService transRuleService;

  @GetMapping("/transaction-rules")
  public ApiResponse getAllRules(
      String coin, String userName, String userId, int currentPage, int pageSize) {
    try {
      return ApiResponse.create(
          State.SUCCESS, transRuleService.selectAll(coin, userName, userId, currentPage, pageSize));
    } catch (Exception e) {
      e.printStackTrace();
      return ApiResponse.create(State.FAIL);
    }
  }

  @PostMapping("/transaction-rules")
  public ApiResponse addAllRules(
      String coin, String userName, String userId, int currentPage, int pageSize) {
    try {
      return null;
    } catch (Exception e) {
      e.printStackTrace();
      return ApiResponse.create(State.FAIL);
    }
  }
}
