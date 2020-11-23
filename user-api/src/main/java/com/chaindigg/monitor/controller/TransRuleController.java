package com.chaindigg.monitor.controller;

import com.chaindigg.monitor.enums.State;
import com.chaindigg.monitor.exception.DataBaseException;
import com.chaindigg.monitor.service.ITransRuleService;
import com.chaindigg.monitor.utils.ApiResponse;
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
  private final ITransRuleService transRuleService;

  @GetMapping("/transaction-rules")
  public ApiResponse getAllRulesByUserId(Integer id, int currentPage, int pageSize) {
    try {
      return ApiResponse.create(
          State.SUCCESS, transRuleService.selectAllById(id, currentPage, pageSize));
    } catch (Exception e) {
      return ApiResponse.create(State.FAIL);
    }
  }

  @PostMapping("/transaction-rules")
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

  @DeleteMapping("/transaction-rules")
  public ApiResponse deleteAllRules(String userName, String coinKind, LocalDateTime AddTime) {
    try {
      return ApiResponse.create(
          State.SUCCESS, transRuleService.delete(userName, coinKind, AddTime));
    } catch (Exception e) {
      e.printStackTrace();
      return ApiResponse.create(State.FAIL);
    }
  }

  @PutMapping("/transaction-rules")
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
