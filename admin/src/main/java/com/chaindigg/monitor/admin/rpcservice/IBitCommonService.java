package com.chaindigg.monitor.admin.rpcservice;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chaindigg.monitor.common.entity.AddrRule;
import com.chaindigg.monitor.common.entity.TransRule;
import com.sulacosoft.bitcoindconnector4j.response.BlockWithTransaction;

import java.util.List;

public interface IBitCommonService {
  void monitor(QueryWrapper addrQueryWrapper, QueryWrapper transQueryWrapper) throws Exception;
  
  void transMonitor(List<TransRule> transRuleList, List<String> transValueList, BlockWithTransaction blockWithTransaction);
  
  void addrMonitor(List<AddrRule> addrRuleList, List<String> addrList, BlockWithTransaction blockWithTransaction);
}
