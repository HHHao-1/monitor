package com.chaindigg.monitor.admin.rpcservice.impl;

import com.chaindigg.monitor.admin.rpcservice.IBitCommonService;
import com.chaindigg.monitor.admin.rpcservice.IBitRpcInitService;
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
    log.info("BTC区块监控beginning");
    try {
      commonService.monitor(rpcUtils.createQueryConditions("BTC")[0], rpcUtils.createQueryConditions("BCH")[1]);
    } catch (Exception e) {
      e.printStackTrace();
      log.info("BCH区块监控异常，ending");
    }
  }
  
  public void bchMonitor() {
    log.info("BCH区块监控beginning");
    try {
      commonService.monitor(rpcUtils.createQueryConditions("BCH")[0], rpcUtils.createQueryConditions("BCH")[1]);
    } catch (Exception e) {
      e.printStackTrace();
      log.info("BCH区块监控异常，ending");
    }
  }
  
  public void ltcMonitor() {
    log.info("LTC区块监控beginning");
    try {
      commonService.monitor(rpcUtils.createQueryConditions("LTC")[0], rpcUtils.createQueryConditions("BCH")[1]);
    } catch (Exception e) {
      e.printStackTrace();
      log.info("LTC区块监控异常，ending");
    }
  }
  
  public void bsvMonitor() {
    log.info("BSV区块监控beginning");
    try {
      commonService.monitor(rpcUtils.createQueryConditions("BSV")[0], rpcUtils.createQueryConditions("BCH")[1]);
    } catch (Exception e) {
      e.printStackTrace();
      log.info("BSV区块监控异常，ending");
    }
  }
}
