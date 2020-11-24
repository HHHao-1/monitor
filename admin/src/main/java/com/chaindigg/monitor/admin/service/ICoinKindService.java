package com.chaindigg.monitor.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaindigg.monitor.common.entity.CoinKind;

import java.util.List;

/**
 * 服务类
 *
 * @author chenghao
 * @since 2020-11-17
 */
public interface ICoinKindService extends IService<CoinKind> {

  List<CoinKind> selectAll(String mainChain, String coinName, int currentPage, int pageSize);

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
