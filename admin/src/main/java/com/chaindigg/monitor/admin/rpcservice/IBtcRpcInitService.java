package com.chaindigg.monitor.admin.rpcservice;


import com.chaindigg.monitor.common.entity.AddrRule;
import com.chaindigg.monitor.common.entity.TransRule;
import com.sulacosoft.bitcoindconnector4j.response.BlockWithTransaction;

import java.util.List;

public interface IBtcRpcInitService {
  void init();
  
  void monitor();
  
  void transMonitor(List<TransRule> transRuleList, List<String> transValueList, BlockWithTransaction blockWithTransaction);
  
  void addrMonitor(List<AddrRule> addrRuleList, List<String> addrList, BlockWithTransaction blockWithTransaction);
}
