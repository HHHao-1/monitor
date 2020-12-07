package com.chaindigg.monitor.admin.api.impl;

import com.chaindigg.monitor.admin.rpcservice.*;
import com.chaindigg.monitor.admin.utils.RpcUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;

@Slf4j
@Component
@PropertySource(value = {"classpath:rpc.properties"})
public class BlockRpcInit implements ApplicationRunner {
  
  @Resource
  private IBtcRpcInitService btcRpcInitService;
  @Resource
  private IBchRpcInitService bchRpcInitService;
  @Resource
  private ILtcRpcInitService ltcRpcInitService;
  
  @Resource
  private IEthRpcInitService ethRpcInitService;
  @Resource
  private IOmniRpcInitService omniRpcInitService;
  
  @Override
  public void run(ApplicationArguments args) {
    RpcUtils.init(new ArrayList<>());
//    try {
//      btcRpcInitService.init();
//      btcRpcInitService.monitor();
//      bchRpcInitService.init();
//      bchRpcInitService.monitor();
//      ltcRpcInitService.init();
//      ltcRpcInitService.monitor();
    ethRpcInitService.init();
    ethRpcInitService.monitor();
//      omniRpcInitService.init();
//      omniRpcInitService.monitor();
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
  }
}


