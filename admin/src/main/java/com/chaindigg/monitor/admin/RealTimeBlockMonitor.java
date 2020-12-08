package com.chaindigg.monitor.admin;

import com.chaindigg.monitor.admin.rpc.service.IBitRpcService;
import com.chaindigg.monitor.admin.rpc.service.IEthRpcService;
import com.chaindigg.monitor.admin.utils.RpcUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@PropertySource(value = {"classpath:rpc.properties"})
public class RealTimeBlockMonitor implements ApplicationRunner {
  // 节点url
  @Value("#{'${btc-rpc-urls}'.split(',')}")
  private List<String> btcUrlList;
  @Value("#{'${bch-rpc-urls}'.split(',')}")
  private List<String> bchUrlList;
  @Value("#{'${ltc-rpc-urls}'.split(',')}")
  private List<String> ltcUrlList;
  @Value("#{'${bsv-rpc-urls}'.split(',')}")
  private List<String> bsvUrlList;
  @Value("#{'${eth-rpc-urls}'.split(',')}")
  private List<String> ethUrlList;
  
  @Resource
  private RpcUtils rpcUtils;
  
  // RpcService
  @Resource
  private IBitRpcService bitRpcInitService;
  @Resource
  private IEthRpcService ethRpcInitService;
  
  @Override
  public void run(ApplicationArguments args) {
    
    // 连接节点
    rpcUtils.bitInit(btcUrlList);
//    rpcUtils.bitInit(bchUrlList);
//    rpcUtils.bitInit(ltcUrlList);
//    rpcUtils.bitInit(bsvUrlList);
    rpcUtils.ethInit(ethUrlList);
    
    // 多币种监控并行运行
    List<String> runList = Arrays.asList("btc", "bch", "ltc", "bsv", "eth");
    runList.parallelStream().forEach(element -> {
      switch (element) {
        case "btc":
          bitRpcInitService.btcMonitor();
          break;
//        case "bch":
//          bitRpcInitService.bchMonitor();
//          break;
//        case "ltc":
//          bitRpcInitService.ltcMonitor();
//          break;
//        case "bsv":
//          bitRpcInitService.bsvMonitor();
//          break;
//        case "eth":
//          ethRpcInitService.ethMonitor();
//          break;
      }
    });
  }
}


