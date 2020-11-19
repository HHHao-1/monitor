package com.chaindigg.monitor.controller;


import com.chaindigg.monitor.entity.CoinKind;
import com.chaindigg.monitor.service.ICoinKindService;
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
 * @since 2020-11-17 14:46:20
 */

@RequiredArgsConstructor
@RestController
public class CoinKindController {
  private final ICoinKindService coinKindService;

  @GetMapping("/coin-kinds")
  public List<CoinKind> getCoinKinds(@Nullable String mainChain, @Nullable String coinName, int currentPage, int pageSize) {
    return coinKindService.selectAll(mainChain, coinName, currentPage, pageSize);
  }

}