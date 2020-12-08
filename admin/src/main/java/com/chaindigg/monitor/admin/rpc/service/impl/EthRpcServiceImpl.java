package com.chaindigg.monitor.admin.rpc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chaindigg.monitor.admin.rpc.service.IEthRpcService;
import com.chaindigg.monitor.admin.utils.DataBaseUtils;
import com.chaindigg.monitor.admin.utils.RpcUtils;
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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@PropertySource(value = {"classpath:config.properties"})
public class EthRpcServiceImpl implements IEthRpcService {
  @Value("${database-retry-num}") // insert重试次数
  private int dataBaseRetryNum;
  
  @Resource
  private AddrRuleMapper addrRuleMapper;
  @Resource
  private TransRuleMapper transRuleMapper;
  @Resource
  private MonitorAddrMapper monitorAddrMapper;
  @Resource
  private MonitorTransMapper monitorTransMapper;
  @Resource
  private RpcUtils rpcUtils;
  
  public void ethMonitor() {
    monitor(rpcUtils.createQueryConditions("ETH")[0], rpcUtils.createQueryConditions("ETH")[1], "ETH");
  }
  
  public void monitor(QueryWrapper addrQueryWrapper, QueryWrapper transQueryWrapper, String coinKind) {
    // region 查询规则
    List<AddrRule> addrRuleList = addrRuleMapper.selectList(addrQueryWrapper);
    List<TransRule> transRuleList = transRuleMapper.selectList(transQueryWrapper);
    List<String> addrList = addrRuleList.stream().map(AddrRule::getAddress).collect(Collectors.toList());
    List<String> transValueList = transRuleList.stream().map(TransRule::getMonitorMinVal).collect(Collectors.toList());
    // endregion
    Long maxBlockHeight = null;
    Long maxBlockHeightOld = null;
    while (true) {
      try {
        maxBlockHeight = ParityPoolUtil.getMaxBlockHeight();
        if (!Objects.equals(maxBlockHeight, maxBlockHeightOld)) {
          log.info(coinKind + "区块监控beginning");
          maxBlockHeightOld = maxBlockHeight;
          RawEthBlock rawEthBlock = ParityPoolUtil.getBlockWithTransaction(maxBlockHeight);
          //      RawEthBlock rawEthBlock = ParityPoolUtil.getBlockWithTransaction(11384081L);
          //      log.info(rawEthBlock.toString());
          List<String> runList = Arrays.asList("addr", "trans");
          runList.stream().parallel().forEach(s -> {
            switch (s) {
              case "addr":
                addrMonitor(addrRuleList, addrList, rawEthBlock, coinKind);
                break;
              case "trans":
                transMonitor(transRuleList, transValueList, rawEthBlock, coinKind);
                break;
            }
          });
        }
      } catch (Exception e) {
        e.printStackTrace();
        log.info("ETH区块监控异常，ending");
      }
    }
  }
  
  /**
   * 大额交易监控
   *
   * @param transRuleList        大额交易监控表
   * @param transValueList       监控额度集合
   * @param blockWithTransaction 区块信息实例对象
   */
  public void transMonitor(List<TransRule> transRuleList, List<String> transValueList,
                           RawEthBlock blockWithTransaction, String coinKind) {
    log.info(coinKind + "区块大额交易监控beginning");
    blockWithTransaction.getTransactions().stream()
        .filter(s -> transValueList.stream().map(BigDecimal::new).filter(x -> x.compareTo(new BigDecimal(s.getValueRaw())) < 0).count() > 0)
        .forEach(txElement -> {
          List<Integer> transIdList = transRuleList.stream()
              .filter(y -> new BigDecimal(y.getMonitorMinVal()).compareTo(new BigDecimal(txElement.getValueRaw())) < 0)
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
              int rows = monitorTransMapper.insert(monitorTrans);
              DataBaseUtils.insertInspect(rows, null, monitorTrans.getTransHash(), monitorTrans, coinKind);
            } catch (Exception e) {
              e.printStackTrace();
            }
          });
        });
    log.info(coinKind + "区块大额交易监控ending");
  }
  
  /**
   * 地址监控
   *
   * @param addrRuleList         地址监控规则列表
   * @param addrList             监控地址集合
   * @param blockWithTransaction 区块信息实例对象
   */
  public void addrMonitor(List<AddrRule> addrRuleList, List<String> addrList, RawEthBlock blockWithTransaction, String coinKind) {
    log.info(coinKind + "区块地址监控beginning");
    blockWithTransaction.getTransactions().stream().filter(s -> addrList.contains(s.getFrom()))
        .forEach(txElement -> {
          List<Integer> addrIdList = addrRuleList.stream()
              .filter(s -> s.getAddress().equals(txElement.getFrom()))
              .map(AddrRule::getId)
              .collect(Collectors.toList());
          addrIdList.forEach(addrId -> {
            final MonitorAddr monitorAddr = new MonitorAddr();
            monitorAddr.setTransHash(txElement.getHash())
                .setUnusualCount("-" + txElement.getValueRaw())
                .setUnusualTime(LocalDateTime.ofEpochSecond(Long.valueOf(blockWithTransaction.getTimestampRaw()), 0, ZoneOffset.ofHours(8)))
                .setAddrRuleId(addrId);
            int rows = monitorAddrMapper.insert(monitorAddr);
            DataBaseUtils.insertInspect(rows, monitorAddr, txElement.getHash(), null, coinKind);
          });
        });
    blockWithTransaction.getTransactions().stream().filter(s -> addrList.contains(s.getTo()))
        .forEach(txElement -> {
          List<Integer> addrIdList = addrRuleList.stream()
              .filter(s -> s.getAddress().equals(txElement.getTo()))
              .map(AddrRule::getId)
              .collect(Collectors.toList());
          addrIdList.forEach(addrId -> {
            final MonitorAddr monitorAddr = new MonitorAddr();
            monitorAddr.setTransHash(txElement.getHash())
                .setUnusualCount("+" + txElement.getValueRaw())
                .setUnusualTime(LocalDateTime.ofEpochSecond(Long.valueOf(blockWithTransaction.getTimestampRaw()), 0, ZoneOffset.ofHours(8)))
                .setAddrRuleId(addrId);
            int rows = monitorAddrMapper.insert(monitorAddr);
            DataBaseUtils.insertInspect(rows, monitorAddr, txElement.getHash(), null, coinKind);
          });
        });
    log.info(coinKind + "区块地址监控ending");
  }
  
}
