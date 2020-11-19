package com.chaindigg.monitor.controller;


import com.chaindigg.monitor.entity.TransRule;
import com.chaindigg.monitor.service.ITransRuleService;
import com.chaindigg.monitor.vo.TransRuleVO;
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
public class TransRuleController {
  private final ITransRuleService transRuleService;

  @GetMapping("/transaction-rules")
  public List<TransRuleVO> getAllRules(@Nullable String coin, @Nullable String userName, @Nullable String userId, int currentPage, int pageSize) {
    return transRuleService.selectAll(coin, userName, userId,currentPage, pageSize);
  }

}