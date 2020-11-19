package com.chaindigg.monitor.controller;


import com.chaindigg.monitor.service.IAddrRuleService;
import com.chaindigg.monitor.vo.AddrRuleVO;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
public class AddrRuleController {
  private final IAddrRuleService addrRuleService;


  @GetMapping("/addr-rules")
  public List<AddrRuleVO> getAllRulesList(@Nullable String event, @Nullable String userName, @Nullable String userId, int currentPage, int pageSize) {
    return addrRuleService.selectAll(event, userName, userId, currentPage, pageSize);
  }

}