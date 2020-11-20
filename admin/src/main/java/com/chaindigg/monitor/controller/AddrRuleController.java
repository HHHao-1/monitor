package com.chaindigg.monitor.controller;


import com.chaindigg.monitor.service.IAddrRuleService;
import com.chaindigg.monitor.utils.ApiResponse;
import com.chaindigg.monitor.vo.AddrRuleVO;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
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
  private final IAddrRuleService addrRuleService;


  @GetMapping("/addr-rules")
  public ApiResponse<AddrRuleVO> getAllRulesList(@Nullable String event, @Nullable String userName, @Nullable String userId, int currentPage, int pageSize) {
    ApiResponse apiResponse = new ApiResponse();
    try {
      return apiResponse.success(addrRuleService.selectAll(event, userName, userId, currentPage, pageSize));
    } catch (Exception e) {
      e.printStackTrace();
      return apiResponse.fail();
    }
  }

}