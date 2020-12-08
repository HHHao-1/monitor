package com.chaindigg.monitor;

import com.chaindigg.monitor.common.utils.RpcUtils;
import com.chaindigg.monitor.service.IEthRpcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Component
@PropertySource(value = {"classpath:eth.properties"})
public class RealTimeBlockMonitor implements ApplicationRunner {
  // 节点url
  @Value("#{'${eth-rpc-urls}'.split(',')}")
  private List<String> ethUrlList;
  
  @Resource
  private RpcUtils rpcUtils;
  
  // RpcService
  @Resource
  private IEthRpcService ethRpcInitService;
  
  @Override
  public void run(ApplicationArguments args) {
    
    rpcUtils.ethInit(ethUrlList);
    ethRpcInitService.ethMonitor();
  }
}


