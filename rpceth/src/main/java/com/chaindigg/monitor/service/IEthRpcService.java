package com.chaindigg.monitor.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chaindigg.monitor.common.entity.AddrRule;
import com.chaindigg.monitor.common.entity.TransRule;
import com.zhifantech.bo.RawEthBlock;

import java.util.List;

public interface IEthRpcService {
  
  void ethMonitor(Long blockHeight);
  
  void monitor(QueryWrapper addrQueryWrapper, QueryWrapper transQueryWrapper, String coinKind, Long blockHeight);
  
  void transMonitor(List<TransRule> transRuleList, List<String> transValueList, RawEthBlock blockWithTransaction, String coinKind);
  
  void addrMonitor(List<AddrRule> addrRuleList, List<String> addrList, RawEthBlock blockWithTransaction, String coinKind);
}
