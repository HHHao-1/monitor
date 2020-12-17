package com.chaindigg.monitor.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaindigg.monitor.common.entity.CoinKind;

import java.util.Map;

/**
 * 服务类
 *
 * @author chenghao
 * @since 2020-11-17
 */
public interface ICoinKindService extends IService<CoinKind> {
  
  Map<String, Object> selectAll(String mainChain, String coinName, Integer currentPage, Integer pageSize);
  
  Boolean add(String mainChain, String coinName, String contract, Integer point);
  
  Boolean delete(String mainChain, String coinName, String contract, Integer point);
  
  Boolean update(
      String mainChain,
      String coinName,
      String contract,
      Integer point,
      String mainChainNew,
      String coinNameNew,
      String contractNew,
      Integer pointNew);
}
