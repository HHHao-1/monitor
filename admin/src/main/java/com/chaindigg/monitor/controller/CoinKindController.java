package com.chaindigg.monitor.controller;


import com.chaindigg.monitor.entity.CoinKind;
import com.chaindigg.monitor.service.ICoinKindService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 服务控制器
 *
 * @author chenghao
 * @since 2020-11-17 14:46:20
 */

@RequiredArgsConstructor
@RestController
public class CoinKindController {
  private final ICoinKindService coinKindService;

  @GetMapping("/coin-kinds")
  public List<CoinKind> getCoinKindList(int currentPage, int pageSize) {
    return coinKindService.selectAll(currentPage, pageSize);
  }

}