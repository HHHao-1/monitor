package com.chaindigg.monitor.controller;

import com.chaindigg.monitor.service.IEthRpcService;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@PropertySource(value = {"classpath:eth.properties"})
public class TestController {
  @Resource
  private IEthRpcService ethRpcService;
  
  
  @GetMapping("/monitor/test/{blockHeight}")
  public String testBlock(@PathVariable Long blockHeight) {
    try {
      ethRpcService.ethMonitor(blockHeight);
      return "成功";
    } catch (Exception e) {
      e.printStackTrace();
      return e.getMessage();
    }
  }
}
