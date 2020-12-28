package com.chaindigg.monitor.admin.controller;

import com.chaindigg.monitor.admin.service.ICoinKindService;
import com.chaindigg.monitor.common.enums.State;
import com.chaindigg.monitor.common.service.ISearchCoinKindService;
import com.chaindigg.monitor.common.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

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
  
  @GetMapping("/coin-kinds")
  public ApiResponse getCoinKinds(
      @Nullable @RequestParam List<String> mainChain, @Nullable @RequestParam List<String> coinName,
      Integer currentPage,
      Integer pageSize) {
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
