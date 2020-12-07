package com.chaindigg.monitor.admin.rpc.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chaindigg.monitor.common.entity.AddrRule;
import com.chaindigg.monitor.common.entity.TransRule;
import com.sulacosoft.bitcoindconnector4j.response.BlockWithTransaction;

import java.util.List;

public interface IBitCommonService {
  void monitor(QueryWrapper addrQueryWrapper, QueryWrapper transQueryWrapper, String coinKind) throws Exception;
  
  void transMonitor(List<TransRule> transRuleList, List<String> transValueList, BlockWithTransaction blockWithTransaction, String coinKind);
  
  void addrMonitor(List<AddrRule> addrRuleList, List<String> addrList, BlockWithTransaction blockWithTransaction, String coinKind);
}
