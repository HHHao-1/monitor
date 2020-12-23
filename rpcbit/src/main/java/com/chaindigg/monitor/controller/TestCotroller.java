package com.chaindigg.monitor.controller;

import com.chaindigg.monitor.service.IBitRpcService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@PropertySource(value = {"classpath:bit.properties"})
public class TestCotroller {
  @Resource
  private IBitRpcService bitRpcService;
  
  @Value("${rpc-coin-kind}")
  private String rpcCoinKind;
  
  
  @GetMapping("/monitor/test/{blockHeight}")
  public String selectExist(@PathVariable Long blockHeight) {
    try {
      bitRpcService.bitMonitor(rpcCoinKind, blockHeight);
      return "成功";
    } catch (Exception e) {
      e.printStackTrace();
      return e.getMessage();
    }
  }
  
}
