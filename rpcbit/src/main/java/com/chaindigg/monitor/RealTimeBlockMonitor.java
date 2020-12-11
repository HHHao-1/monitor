package com.chaindigg.monitor;


import com.chaindigg.monitor.service.IBitRpcService;
import com.chaindigg.monitor.utils.RpcUtils;
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
@PropertySource(value = {"classpath:bit.properties"})
public class RealTimeBlockMonitor implements ApplicationRunner {
  // 节点url
  @Value("#{'${rpc-urls}'.split(',')}")
  private List<String> bitUrlList;
  @Value("${rpc-coin-kind}")
  private String rpcCoinKind;
  
  @Resource
  private RpcUtils rpcUtils;
  
  // RpcService
  @Resource
  private IBitRpcService bitRpcInitService;
  
  @Override
  public void run(ApplicationArguments args) {
    
    // 连接节点
    rpcUtils.bitInit(bitUrlList);
    bitRpcInitService.bitMonitor(rpcCoinKind);
    
  }
}


