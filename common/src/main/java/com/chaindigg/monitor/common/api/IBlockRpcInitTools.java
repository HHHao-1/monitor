package com.chaindigg.monitor.common.api;

import com.chaindigg.monitor.common.entity.AddrRule;
import com.chaindigg.monitor.common.entity.MonitorAddr;
import com.chaindigg.monitor.common.entity.MonitorTrans;
import com.chaindigg.monitor.common.entity.TransRule;
import com.sulacosoft.bitcoindconnector4j.response.BlockWithTransaction;
import com.sulacosoft.bitcoindconnector4j.response.RawTransaction;

import java.util.List;

public interface IBlockRpcInitTools {
    void monitor();

    void addrMonitor(List<AddrRule> addrRuleList, List<String> addrList, BlockWithTransaction blockWithTransaction);

    void insertMonitorData(RawTransaction txElement, RawTransaction.Vout vout, List<AddrRule> addrRuleList, List<String> addrList,
                           List<String> voutAddresses, List<TransRule> transRuleList, List<String> transValueList, String vinValue,
                           String voutValue, long blocktime, Boolean inOrOut);

    void insertInspect(int rows, MonitorAddr monitorAddr, String transHash, MonitorTrans monitorTrans);
}
