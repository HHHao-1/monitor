package com.chaindigg.monitor.admin.rpcservice;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chaindigg.monitor.common.entity.AddrRule;
import com.chaindigg.monitor.common.entity.TransRule;
import com.zhifantech.bo.RawEthBlock;

import java.util.List;

public interface IEthRpcInitService {
  
  void ethMonitor();
  
  void monitor(QueryWrapper addrQueryWrapper, QueryWrapper transQueryWrapper);
  
  void transMonitor(List<TransRule> transRuleList, List<String> transValueList, RawEthBlock blockWithTransaction);
  
  void addrMonitor(List<AddrRule> addrRuleList, List<String> addrList, RawEthBlock blockWithTransaction);
}
