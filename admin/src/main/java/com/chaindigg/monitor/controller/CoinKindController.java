package com.chaindigg.monitor.controller;

import com.chaindigg.monitor.enums.State;
import com.chaindigg.monitor.service.ICoinKindService;
import com.chaindigg.monitor.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
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
      @Nullable String mainChain, @Nullable String coinName, int currentPage, int pageSize) {
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
      String mainChain, @Nullable String coinName, @Nullable String contract, Integer point) {
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
      String mainChain, @Nullable String coinName, @Nullable String contract, Integer point) {
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
      @Nullable String coinName,
      @Nullable String contract,
      Integer point,
      @Nullable String mainChainNew,
      @Nullable String coinNameNew,
      @Nullable String contractNew,
      @Nullable Integer pointNew) {
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
