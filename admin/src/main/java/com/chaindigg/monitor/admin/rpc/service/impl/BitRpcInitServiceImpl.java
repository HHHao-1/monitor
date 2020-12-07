package com.chaindigg.monitor.admin.rpc.service.impl;

import com.chaindigg.monitor.admin.rpc.service.IBitCommonService;
import com.chaindigg.monitor.admin.rpc.service.IBitRpcInitService;
import com.chaindigg.monitor.admin.utils.RpcUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class BitRpcInitServiceImpl implements IBitRpcInitService {
  
  @Resource
  private IBitCommonService commonService;
  @Resource
  private RpcUtils rpcUtils;
  
  public void btcMonitor() {
    try {
      commonService.monitor(rpcUtils.createQueryConditions("BTC")[0], rpcUtils.createQueryConditions("BCH")[1], "BTC");
    } catch (Exception e) {
      e.printStackTrace();
      log.info("BTC区块监控异常，ending");
    }
  }
  
  public void bchMonitor() {
    try {
      commonService.monitor(rpcUtils.createQueryConditions("BCH")[0], rpcUtils.createQueryConditions("BCH")[1], "BCH");
    } catch (Exception e) {
      e.printStackTrace();
      log.info("BCH区块监控异常，ending");
    }
  }
  
  public void ltcMonitor() {
    try {
      commonService.monitor(rpcUtils.createQueryConditions("LTC")[0], rpcUtils.createQueryConditions("BCH")[1], "LTC");
    } catch (Exception e) {
      e.printStackTrace();
      log.info("LTC区块监控异常，ending");
    }
  }
  
  public void bsvMonitor() {
    log.info("BSV区块监控beginning");
    try {
      commonService.monitor(rpcUtils.createQueryConditions("BSV")[0], rpcUtils.createQueryConditions("BCH")[1], "BSV");
    } catch (Exception e) {
      e.printStackTrace();
      log.info("BSV区块监控异常，ending");
    }
  }
}
