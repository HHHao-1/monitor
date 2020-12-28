package com.chaindigg.monitor.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaindigg.monitor.common.dao.CoinKindMapper;
import com.chaindigg.monitor.common.entity.CoinKind;
import com.chaindigg.monitor.common.service.ISearchCoinKindService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchCoinKindServiceImpl extends ServiceImpl<CoinKindMapper, CoinKind>
    implements ISearchCoinKindService {
  
  @Override
  public List<String> searchCoinKind() {
    List<String> list = new ArrayList<>();
    this.list().stream().forEach(e -> list.add(e.getCoinName()));
    return list;
  }
  
  @Override
  public List<String> searchCoinMain() {
    List<String> list = new ArrayList<>();
    this.list().stream().forEach(e -> list.add(e.getMainChain()));
    return list;
  }
  
  @Override
  public List<String> searchCoinContract(String[] coins) {
    List<String> list = new ArrayList<>();
    QueryWrapper<CoinKind> queryWrapper = new QueryWrapper<>();
    queryWrapper.in("coin_name", coins);
    List<String> res = this.list(queryWrapper).stream().map(CoinKind::getContractAddr).collect(Collectors.toList());
    return res;
  }
}
