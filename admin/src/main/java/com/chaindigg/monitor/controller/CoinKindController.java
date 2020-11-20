package com.chaindigg.monitor.controller;


import com.chaindigg.monitor.entity.CoinKind;
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
  public ApiResponse<CoinKind> getCoinKinds(@Nullable String mainChain, @Nullable String coinName, int currentPage, int pageSize) {
    ApiResponse apiResponse = new ApiResponse();
    try {
      return apiResponse.success(coinKindService.selectAll(mainChain, coinName, currentPage, pageSize));
    } catch (Exception e) {
      e.printStackTrace();
      return apiResponse.fail();
    }
  }

  @PostMapping("/coin-kinds")
  public ApiResponse<Boolean> addCoinKind(String mainChain, @Nullable String coinName, @Nullable String contract, Integer point) {
    ApiResponse apiResponse = new ApiResponse();
    try {
      return apiResponse.success(coinKindService.add(mainChain, coinName, contract, point));
    } catch (Exception e) {
      e.printStackTrace();
      return apiResponse.fail();
    }
  }

  @DeleteMapping("/coin-kinds")
  public ApiResponse<Boolean> deleteCoinKind(String mainChain, @Nullable  String coinName, @Nullable String contract, Integer point) {
    ApiResponse apiResponse = new ApiResponse();
    try {
     return apiResponse.success(coinKindService.delete(mainChain, coinName, contract, point));
    } catch (Exception e) {
      e.printStackTrace();
      return apiResponse.fail();
    }
  }

  @PutMapping("/coin-kinds")
  public ApiResponse<Boolean> updateCoinKind(String mainChain, @Nullable String coinName, @Nullable String contract, Integer point,
                                             @Nullable String mainChainNew,
                                             @Nullable String coinNameNew,
                                             @Nullable String contractNew, @Nullable Integer pointNew) {
    ApiResponse apiResponse = new ApiResponse();
    try {
      return apiResponse.success(coinKindService.update(mainChain, coinName, contract, point, mainChainNew, coinNameNew, contractNew, pointNew));
    } catch (Exception e) {
      e.printStackTrace();
      return apiResponse.fail();
    }
  }

}