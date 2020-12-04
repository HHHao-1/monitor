package com.chaindigg.monitor.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chaindigg.monitor.admin.service.IEthRpcInitService;
import com.chaindigg.monitor.admin.utils.DataBaseUtils;
import com.chaindigg.monitor.common.dao.AddrRuleMapper;
import com.chaindigg.monitor.common.dao.MonitorAddrMapper;
import com.chaindigg.monitor.common.dao.MonitorTransMapper;
import com.chaindigg.monitor.common.dao.TransRuleMapper;
import com.chaindigg.monitor.common.entity.AddrRule;
import com.chaindigg.monitor.common.entity.MonitorAddr;
import com.chaindigg.monitor.common.entity.MonitorTrans;
import com.chaindigg.monitor.common.entity.TransRule;
import com.zhifantech.bo.RawEthBlock;
import com.zhifantech.util.ParityPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@PropertySource(value = {"classpath:rpc.properties"})
public class EthRpcInitServiceImpl implements IEthRpcInitService {
  @Value("${database-retry-num}") // insert重试次数
  private int dataBaseRetryNum;
  @Value("#{'${eth-urls}'.split(',')}")
  private List<String> urlList; // 节点url
  @Value("${eth-retry-num}")
  private int rpcRetryNum; // 链接失败重试次数
  @Value("${eth-retry-interv}")
  private int rpcRetryInterv; // 重新链接间隔时间
  
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
      ParityPoolUtil.init(urlList, rpcRetryNum, rpcRetryInterv);
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
      addrQueryWrapper.eq("coin_kind", "ETH");
      QueryWrapper transQueryWrapper = new QueryWrapper();
      transQueryWrapper.select("id", "monitor_min_val").eq("state", 1);
      transQueryWrapper.in("coin_kind", "ETH");
      List<AddrRule> addrRuleList = addrRuleMapper.selectList(addrQueryWrapper);
      List<TransRule> transRuleList = transRuleMapper.selectList(transQueryWrapper);
      List<String> addrList = addrRuleList.stream().map(AddrRule::getAddress).collect(Collectors.toList());
      List<String> transValueList = transRuleList.stream().map(TransRule::getMonitorMinVal).collect(Collectors.toList());
      // endregion
      
      RawEthBlock rawEthBlock = ParityPoolUtil.getBlockWithTransaction(11384081L);
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
  
  /**
   * 大额交易监控
   *
   * @param transRuleList        大额交易监控表
   * @param transValueList       监控额度集合
   * @param blockWithTransaction 区块信息实例对象
   */
  public void transMonitor(List<TransRule> transRuleList, List<String> transValueList, RawEthBlock blockWithTransaction) {
    log.info("区块大额交易监控beginning");
    List<MonitorTrans> monitorTransList = new ArrayList<>();
    // 解决lambda表达式中外部声明的变量必须final的问题
    Object[] monitorTransListS = {monitorTransList};
    blockWithTransaction.getTransactions().stream().parallel().filter(s -> transValueList.contains(s))
        .forEach(txElement -> {
          List<Integer> transIdList = transRuleList.stream()
              .filter(s -> new BigDecimal(s.getMonitorMinVal()).compareTo(new BigDecimal(txElement.getValueRaw())) < 0)
              .map(TransRule::getId)
              .collect(Collectors.toList());
          transIdList.forEach(transId -> {
            MonitorTrans monitorTrans = new MonitorTrans();
            try {
              monitorTrans
                  .setTransHash(txElement.getHash())
                  .setUnusualCount(txElement.getValueRaw())
                  .setUnusualTime(LocalDateTime.ofEpochSecond(Long.valueOf(blockWithTransaction.getTimestampRaw()), 0, ZoneOffset.ofHours(8)))
                  .setTransRuleId(transId)
                  .setToAddress(txElement.getTo())
                  .setFromAddress(txElement.getFrom());
            } catch (Exception e) {
              e.printStackTrace();
            }
            ((List<MonitorTrans>) monitorTransListS[0]).add(monitorTrans);
          });
          if (((List<MonitorTrans>) monitorTransListS[0]).size() != 0) {
            for (MonitorTrans monitorTrans : ((List<MonitorTrans>) monitorTransListS[0])) {
              int rows = monitorTransMapper.insert(monitorTrans);
              DataBaseUtils.insertInspect(rows, null, monitorTrans.getTransHash(), monitorTrans);
            }
          }
          ((List<MonitorTrans>) monitorTransListS[0]).clear();
        });
    log.info("区块大额交易监控ending");
  }
  
  /**
   * 地址监控
   *
   * @param addrRuleList         地址监控规则列表
   * @param addrList             监控地址集合
   * @param blockWithTransaction 区块信息实例对象
   */
  public void addrMonitor(List<AddrRule> addrRuleList, List<String> addrList, RawEthBlock blockWithTransaction) {
    log.info("区块地址监控beginning");
    blockWithTransaction.getTransactions().stream().parallel().filter(s -> addrList.contains(s))
        .forEach(txElement -> {
          List<Integer> addrIdList = addrRuleList.stream()
              .filter(s -> s.getAddress().equals(txElement.getFrom()) || s.getAddress().equals(txElement.getTo()))
              .map(AddrRule::getId)
              .collect(Collectors.toList());
          addrIdList.forEach(addrId -> {
            final MonitorAddr monitorAddr = new MonitorAddr();
            monitorAddr.setTransHash(txElement.getHash())
                .setUnusualCount(txElement.getValueRaw())
                .setUnusualTime(LocalDateTime.ofEpochSecond(Long.valueOf(blockWithTransaction.getTimestampRaw()), 0, ZoneOffset.ofHours(8)))
                .setAddrRuleId(addrId);
            int rows = monitorAddrMapper.insert(monitorAddr);
            DataBaseUtils.insertInspect(rows, monitorAddr, txElement.getHash(), null);
          });
        });
    log.info("区块地址监控ending");
  }
  
}
