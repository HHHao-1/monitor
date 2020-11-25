package com.chaindigg.monitor.userapi.controller;

import com.chaindigg.monitor.common.enums.State;
import com.chaindigg.monitor.common.service.ISearchCoinKindService;
import com.chaindigg.monitor.common.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CoinKindController {

  /**
   * 服务控制器
   *
   * @author chenghao
   * @since 2020-11-25 17:40:50
   */
  private final ISearchCoinKindService searchCoinKindService;

  @GetMapping("/coinlist")
  public ApiResponse getCoinList() {
    try {
      return ApiResponse.create(State.SUCCESS, searchCoinKindService.searchCoinKind());
    } catch (Exception e) {
      e.printStackTrace();
      return ApiResponse.create(State.FAIL);
    }
  }
}
