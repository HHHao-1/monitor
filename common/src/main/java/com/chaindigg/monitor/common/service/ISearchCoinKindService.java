package com.chaindigg.monitor.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaindigg.monitor.common.entity.CoinKind;

import java.util.List;

public interface ISearchCoinKindService extends IService<CoinKind> {
  List<String> searchCoinKind();
  
  List<String> searchCoinMain();
  
  List<String> searchCoinContract(String[] coins);
}
