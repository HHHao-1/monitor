package com.chaindigg.monitor.admin.rpcservice.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chaindigg.monitor.admin.rpcservice.IOmniRpcInitService;
import com.chaindigg.monitor.common.dao.AddrRuleMapper;
import com.chaindigg.monitor.common.dao.MonitorAddrMapper;
import com.chaindigg.monitor.common.dao.MonitorTransMapper;
import com.chaindigg.monitor.common.dao.TransRuleMapper;
import com.chaindigg.monitor.common.entity.AddrRule;
import com.chaindigg.monitor.common.entity.TransRule;
import com.zhifantech.util.OmniPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@PropertySource(value = {"classpath:rpc.properties"})
public class OmniRpcInitServiceImpl implements IOmniRpcInitService {
  @Value("${database-retry-num}") // insert重试次数
  private int dataBaseRetryNum;
  @Value("#{'${omni-rpc-urls}'.split(',')}")
  private List<String> urlList; // 节点url
  @Value("${omni-retry-num}")
  private int rpcRetryNum; // 链接失败重试次数
  @Value("${omni-retry-interv}")
  private int rpcRetryInterv; // 重新链接间隔时间
  @Value("${omni-user-name}")
  private String username; // 用户名
  @Value("${omni-password}")
  private String password; // 密码
  
  @Resource
  private AddrRuleMapper addrRuleMapper;
  @Resource
  private TransRuleMapper transRuleMapper;
  @Resource
  private MonitorAddrMapper monitorAddrMapper;
  @Resource
  private MonitorTransMapper monitorTransMapper;
  
  public void init() {
    try {
      OmniPoolUtil.init(urlList, username, password, rpcRetryNum, rpcRetryInterv);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public void monitor() {
    log.info("区块监控beginning");
    try {
      // region 查询规则
      QueryWrapper addrQueryWrapper = new QueryWrapper();
      addrQueryWrapper.select("id", "address").eq("state", 1);
      addrQueryWrapper.eq("coin_kind", "OMNI");
      QueryWrapper transQueryWrapper = new QueryWrapper();
      transQueryWrapper.select("id", "monitor_min_val").eq("state", 1);
      transQueryWrapper.in("coin_kind", "OMNI");
      List<AddrRule> addrRuleList = addrRuleMapper.selectList(addrQueryWrapper);
      List<TransRule> transRuleList = transRuleMapper.selectList(transQueryWrapper);
      List<String> addrList = addrRuleList.stream().map(AddrRule::getAddress).collect(Collectors.toList());
      List<String> transValueList = transRuleList.stream().map(TransRule::getMonitorMinVal).collect(Collectors.toList());
      // endregion
      
      Long raw = OmniPoolUtil.getMaxBlockHeight();
//      List<String> runList = new ArrayList();
//      runList.add("addr");
//      runList.add("trans");
//      runList.stream().parallel().forEach(s -> {
//        switch (s) {
//          case "addr":
//            log.info("线程：" + Thread.currentThread().getName());
//            addrMonitor(addrRuleList, addrList, rawEthBlock);
//            break;
//          case "trans":
//            log.info("线程：" + Thread.currentThread().getName());
//            transMonitor(transRuleList, transValueList, rawEthBlock);
//            break;
//        }
//      });
    } catch (Exception e) {
      e.printStackTrace();
      log.info("区块监控异常，ending");
    }
  }
}
