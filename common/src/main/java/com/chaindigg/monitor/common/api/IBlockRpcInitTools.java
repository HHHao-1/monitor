package com.chaindigg.monitor.common.api;

import com.chaindigg.monitor.common.entity.AddrRule;
import com.chaindigg.monitor.common.entity.MonitorAddr;
import com.chaindigg.monitor.common.entity.MonitorTrans;
import com.sulacosoft.bitcoindconnector4j.response.BlockWithTransaction;
import com.zhifantech.api.Rpc;

import java.util.List;

public interface IBlockRpcInitTools {
  List<Rpc> init() throws Exception;
  
  void monitor();
  
  void addrMonitor(List<AddrRule> addrRuleList, List<String> addrList, BlockWithTransaction blockWithTransaction);

//  String insertMonitorData(RawTransaction txElement, RawTransaction.Vout vout, List<AddrRule> addrRuleList, List<String> addrList,
//                           String voutAddresses, List<TransRule> transRuleList, List<String> transValueList, String vinValue,
//                           String voutValue, long blocktime, Boolean inOrOut);
  
  void insertInspect(int rows, MonitorAddr monitorAddr, String transHash, MonitorTrans monitorTrans);
}
