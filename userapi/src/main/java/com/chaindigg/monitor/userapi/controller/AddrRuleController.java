package com.chaindigg.monitor.userapi.controller;

import com.chaindigg.monitor.common.enums.State;
import com.chaindigg.monitor.common.exception.DataBaseException;
import com.chaindigg.monitor.common.service.IAddrRuleService;
import com.chaindigg.monitor.common.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
public class AddrRuleController {
  private final IAddrRuleService addrRuleService;
  
  @GetMapping("/addr-rules")
  public ApiResponse getAllRulesByUserId(Integer id, Integer currentPage, Integer pageSize) {
    try {
      return ApiResponse.create(
          State.SUCCESS, addrRuleService.selectAllById(id, currentPage, pageSize));
    } catch (Exception e) {
      return ApiResponse.create(State.FAIL);
    }
  }
  
  @GetMapping("/addr-rules/id")
  public ApiResponse getAllRulesById(Integer id) {
    try {
      return ApiResponse.create(
          State.SUCCESS, addrRuleService.selectAllByUId(id));
    } catch (Exception e) {
      return ApiResponse.create(State.FAIL);
    }
  }
  
  @PostMapping("/addr-rules")
  public ApiResponse addAllAddrRules(@RequestBody List<Map<String, Object>> list) {
    try {
      return ApiResponse.create(State.SUCCESS, addrRuleService.add(list));
    } catch (DataBaseException e) {
      e.printStackTrace();
      return ApiResponse.create(e.state);
    } catch (Exception e) {
      e.printStackTrace();
      return ApiResponse.create(State.FAIL);
    }
  }
  
  @DeleteMapping("/addr-rules")
  public ApiResponse deleteAllRules(Integer id) {
    try {
      return ApiResponse.create(State.SUCCESS, addrRuleService.delete(id));
    } catch (Exception e) {
      e.printStackTrace();
      return ApiResponse.create(State.FAIL);
    }
  }
  
  @PutMapping("/addr-rules")
  public ApiResponse updateAllRules(@RequestBody List<Map<String, Object>> list) {
    try {
      return ApiResponse.create(State.SUCCESS, addrRuleService.update(list));
    } catch (DataBaseException e) {
      e.printStackTrace();
      return ApiResponse.create(e.state);
    } catch (Exception e) {
      e.printStackTrace();
      return ApiResponse.create(State.FAIL);
    }
  }
}
