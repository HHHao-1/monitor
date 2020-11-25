package com.chaindigg.monitor.admin.controller;

import com.chaindigg.monitor.admin.service.IAddrRuleVOService;
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
  private final IAddrRuleVOService addrRuleVOService;
  private final IAddrRuleService addrRuleService;

  @GetMapping("/addr-rules/search")
  public ApiResponse selectExist(String name) {
    try {
      return ApiResponse.create(State.SUCCESS, "用户id：" + addrRuleService.searchUserId(name));
    } catch (Exception e) {
      e.printStackTrace();
      return ApiResponse.create(State.USER_NOT_EXIST);
    }
  }

  @GetMapping("/addr-rules")
  public ApiResponse getAllRules(
      String event, String userName, String userId, int currentPage, int pageSize) {
    try {
      return ApiResponse.create(
          State.SUCCESS,
          addrRuleVOService.selectAll(event, userName, userId, currentPage, pageSize));
    } catch (Exception e) {
      e.printStackTrace();
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
  public ApiResponse updateAllRules(List<Map<String, Object>> list) {
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
