package com.chaindigg.monitor.controller;

import com.chaindigg.monitor.enums.State;
import com.chaindigg.monitor.service.ICoinKindService;
import com.chaindigg.monitor.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
  public ApiResponse getCoinKinds(
      String mainChain, String coinName, int currentPage, int pageSize) {
    try {
      return ApiResponse.create(
          State.SUCCESS, coinKindService.selectAll(mainChain, coinName, currentPage, pageSize));
    } catch (Exception e) {
      e.printStackTrace();
      return ApiResponse.create(State.FAIL);
    }
  }

  @PostMapping("/coin-kinds")
  public ApiResponse addCoinKind(
      String mainChain, String coinName, String contract, Integer point) {
    try {
      return ApiResponse.create(
          State.SUCCESS, coinKindService.add(mainChain, coinName, contract, point));
    } catch (Exception e) {
      e.printStackTrace();
      return ApiResponse.create(State.FAIL);
    }
  }

  @DeleteMapping("/coin-kinds")
  public ApiResponse deleteCoinKind(
      String mainChain, String coinName, String contract, Integer point) {
    try {
      return ApiResponse.create(
          State.SUCCESS, coinKindService.delete(mainChain, coinName, contract, point));
    } catch (Exception e) {
      e.printStackTrace();
      return ApiResponse.create(State.FAIL);
    }
  }

  @PutMapping("/coin-kinds")
  public ApiResponse updateCoinKind(
      String mainChain,
      String coinName,
      String contract,
      Integer point,
      String mainChainNew,
      String coinNameNew,
      String contractNew,
      Integer pointNew) {
    try {
      return ApiResponse.create(
          State.SUCCESS,
          coinKindService.update(
              mainChain,
              coinName,
              contract,
              point,
              mainChainNew,
              coinNameNew,
              contractNew,
              pointNew));
    } catch (Exception e) {
      e.printStackTrace();
      return ApiResponse.create(State.FAIL);
    }
  }
}
