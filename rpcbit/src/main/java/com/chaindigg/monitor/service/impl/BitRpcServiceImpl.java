package com.chaindigg.monitor.service.impl;

import com.chaindigg.monitor.service.IBitCommonService;
import com.chaindigg.monitor.service.IBitRpcService;
import com.chaindigg.monitor.utils.RpcUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class BitRpcServiceImpl implements IBitRpcService {
  
  @Resource
  private IBitCommonService commonService;
  @Resource
  private RpcUtils rpcUtils;
  
  public void bitMonitor(String coinKind) {
    try {
      commonService.monitor(rpcUtils.createQueryConditions(coinKind)[0], rpcUtils.createQueryConditions(coinKind)[1],
          coinKind);
    } catch (Exception e) {
      e.printStackTrace();
      log.info(coinKind + "区块监控异常，ending");
    }
  }
}
