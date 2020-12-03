package com.chaindigg.monitor.admin.api.impl;

import com.chaindigg.monitor.admin.service.IBtcRpcInitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@PropertySource(value = {"classpath:rpc.properties"})
public class BlockRpcInit implements ApplicationRunner {
  
  @Resource
  private IBtcRpcInitService blockRpcInitTools;
  
  @Override
  public void run(ApplicationArguments args) {
    try {
      blockRpcInitTools.init();
      blockRpcInitTools.monitor();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}


