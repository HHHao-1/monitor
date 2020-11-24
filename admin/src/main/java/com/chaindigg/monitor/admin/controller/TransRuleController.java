package com.chaindigg.monitor.admin.controller;

import com.chaindigg.monitor.admin.service.ITransRuleVOService;
import com.chaindigg.monitor.common.enums.State;
import com.chaindigg.monitor.common.exception.DataBaseException;
import com.chaindigg.monitor.common.service.ITransRuleService;
import com.chaindigg.monitor.common.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 服务控制器
 *
 * @author chenghao
 * @since 2020-11-16 17:40:50
 */
@RequiredArgsConstructor
@RestController
public class TransRuleController {
  private final ITransRuleVOService transRuleVOService;
  private final ITransRuleService transRuleService;

  @GetMapping("/trans-rules")
  public ApiResponse getTransRules(
      String coin, String userName, String userId, int currentPage, int pageSize) {
    try {
      return ApiResponse.create(
          State.SUCCESS,
          transRuleVOService.selectAll(coin, userName, userId, currentPage, pageSize));
    } catch (Exception e) {
      e.printStackTrace();
      return ApiResponse.create(State.FAIL);
    }
  }

  @PostMapping("/trans-rules")
  public ApiResponse addAllTransRules(List<Map<String, Object>> list) {
    try {
      return ApiResponse.create(State.SUCCESS, transRuleService.add(list));
    } catch (DataBaseException e) {
      e.printStackTrace();
      return ApiResponse.create(e.state);
    } catch (Exception e) {
      e.printStackTrace();
      return ApiResponse.create(State.FAIL);
    }
  }

  @DeleteMapping("/trans-rules")
  public ApiResponse deleteAllRules(String userName, String coinKind, LocalDateTime AddTime) {
    try {
      return ApiResponse.create(
          State.SUCCESS, transRuleService.delete(userName, coinKind, AddTime));
    } catch (Exception e) {
      e.printStackTrace();
      return ApiResponse.create(State.FAIL);
    }
  }

  @PutMapping("/trans-rules")
  public ApiResponse updateAllRules(List<Map<String, Object>> list) {
    try {
      return ApiResponse.create(State.SUCCESS, transRuleService.update(list));
    } catch (DataBaseException e) {
      e.printStackTrace();
      return ApiResponse.create(e.state);
    } catch (Exception e) {
      e.printStackTrace();
      return ApiResponse.create(State.FAIL);
    }
  }
}
