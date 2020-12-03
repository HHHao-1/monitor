package com.chaindigg.monitor.common.api.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chaindigg.monitor.common.api.IBlockRpcInitTools;
import com.chaindigg.monitor.common.dao.AddrRuleMapper;
import com.chaindigg.monitor.common.dao.MonitorAddrMapper;
import com.chaindigg.monitor.common.dao.MonitorTransMapper;
import com.chaindigg.monitor.common.dao.TransRuleMapper;
import com.chaindigg.monitor.common.entity.AddrRule;
import com.chaindigg.monitor.common.entity.MonitorAddr;
import com.chaindigg.monitor.common.entity.MonitorTrans;
import com.chaindigg.monitor.common.entity.TransRule;
import com.sulacosoft.bitcoindconnector4j.response.BlockWithTransaction;
import com.sulacosoft.bitcoindconnector4j.response.RawTransaction;
import com.zhifantech.api.Rpc;
import com.zhifantech.api.impl.BtcRpc;
import com.zhifantech.strategy.SeqRetryStrategy;
import com.zhifantech.util.BitcoindPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@PropertySource(value = {"classpath:rpc.properties"})
public class BlockRpcInitToolsImpl implements IBlockRpcInitTools {
  
  @Value("${database-retry-num}") // insert重试次数
  private int dataBaseRetryNum;
  @Value("#{'${rpc-urls}'.split(',')}")
  private List<String> urlList; // 节点url
  @Value("${rpc-retry-num}")
  private int rpcRetryNum; // 链接失败重试次数
  @Value("${rpc-retry-interv}")
  private int rpcRetryInterv; // 重新链接间隔时间
  @Value("${rpc-user-name}")
  private String username; // 用户名
  @Value("${rpc-password}")
  private String password; // 密码
  
  @Resource
  private AddrRuleMapper addrRuleMapper;
  @Resource
  private TransRuleMapper transRuleMapper;
  @Resource
  private MonitorAddrMapper monitorAddrMapper;
  @Resource
  private MonitorTransMapper monitorTransMapper;
  
  private SeqRetryStrategy seqRetryStrategy;
  private Class clazz = BtcRpc.class;
  
  public List<Rpc> init() throws Exception {
    ArrayList rpcList = new ArrayList();
    Iterator var6 = urlList.iterator();
    while (var6.hasNext()) {
      String url = (String) var6.next();
      Rpc rpc = new BtcRpc();
      boolean status = rpc.init(url, username, password);
      if (status) {
        rpcList.add(rpc);
      }
    }
    seqRetryStrategy = new SeqRetryStrategy(rpcList, rpcRetryNum, rpcRetryInterv);
    return BitcoindPoolUtil.init(urlList, username, password, rpcRetryNum, rpcRetryInterv);
  }
  
  public void monitor() {
    log.info("区块监控beginning");
    try {
      // 查询规则
      QueryWrapper addrQueryWrapper = new QueryWrapper();
      addrQueryWrapper.select("id", "address");
      QueryWrapper transQueryWrapper = new QueryWrapper();
      transQueryWrapper.select("id", "monitor_min_val");
      List<AddrRule> addrRuleList = addrRuleMapper.selectList(addrQueryWrapper);
      List<TransRule> transRuleList = transRuleMapper.selectList(transQueryWrapper);
      List<String> addrList = addrRuleList.stream().map(AddrRule::getAddress).collect(Collectors.toList());
      List<String> transValueList = transRuleList.stream().map(TransRule::getMonitorMinVal).collect(Collectors.toList());
      //      Long maxBlockHeight;
      //      while (true) {
      //      Long maxBlockHeightOld = maxBlockHeight;
      //      maxBlockHeight = BitcoindPoolUtil.getMaxBlockHeight();
      //      if (!Objects.equals(maxBlockHeight, maxBlockHeightOld)) {
      //        BlockWithTransaction blockWithTransaction =
      // BitcoindPoolUtil.getBlock(maxBlockHeight);
      BlockWithTransaction blockWithTransaction = BitcoindPoolUtil.getBlock(659314);
      List<String> runList = new ArrayList();
      runList.add("addr");
      runList.add("trans");
      runList.stream().parallel().forEach(s -> {
        if (s.equals("addr")) {
          addrMonitor(addrRuleList, addrList, blockWithTransaction);
        }
        if (s.equals("trans")) {
          transMonitor(transRuleList, transValueList, blockWithTransaction);
        }
      });
      //      }
      //      }
      //      Thread.sleep(3000);
      //      }
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
  public void transMonitor(List<TransRule> transRuleList, List<String> transValueList, BlockWithTransaction blockWithTransaction) {
    log.info("区块大额交易监控beginning");
    List<String> vinAddrList = new ArrayList<>();
    Object[] vinAddrListS = {vinAddrList};
    List<MonitorTrans> monitorTransList = new ArrayList<>();
    Object[] monitorTransListS = {monitorTransList};
    List<RawTransaction.Vout> existList = new ArrayList<>();
    Object[] existListS = {existList};
    blockWithTransaction.getTx().stream().parallel()
        .forEach(txElement -> {
          // 输出地址去重累加
          List<RawTransaction.Vout> txVout = addrAddUp(txElement);
//          List<RawTransaction.Vout> txVout;
          txVout.stream().parallel()
              .forEach(voutElement -> {
                if (voutElement.getValue() != null) {
                  try {
                    String voutValue = voutElement.getValue().toPlainString();
                    // 根据规则配配到的元素
                    String exist = null;
                    // 大额交易匹配
                    if (transRuleList != null) {
                      // 扫描交易vout
                      if (voutValue != null) {
                        List<String> list = transValueList.stream()
                            .filter(s -> new BigDecimal(s).compareTo(new BigDecimal(voutValue)) < 0)
                            .collect(Collectors.toList());
                        if (list.size() != 0) {
                          try {
                            exist = voutElement.getScriptPubKey().getAddresses().get(0);
                          } catch (Exception e) {
                            e.printStackTrace();
                          }
                        }
                      }
                      if (exist != null) {
                        String unusualCount = voutElement.getValue().toPlainString();

//                        // 交易输出地址
//                        txElement.getVin().stream().parallel()
//                            .forEach(vinElement -> {
//                              if (vinElement.getTxid() != null) {
//                                try {
//                                  RawTransaction.Vout vinOut = BitcoindPoolUtil.getVout(vinElement.getTxid(), vinElement.getVout());
//                                  List<String> vinAddr = vinOut.getScriptPubKey().getAddresses();
//                                  if (vinAddr.size() != 0) {
//                                    String vinAddress = vinAddr.get(0);
//                                    ((List<String>) vinAddrListS[0]).add(vinAddress);
//                                  }
//                                } catch (Exception e) {
//                                  e.printStackTrace();
//                                  log.info("获取交易输出地址异常");
//                                }
//                              }
//                            });
                        // 判断匹配地址是否是找零地址
                        if (!vinAddrList.contains(exist)) {
                          ((List<RawTransaction.Vout>) existListS[0]).add(voutElement);
                        }
                      }
                    }
                  } catch (Exception e) {
                    e.printStackTrace();
                    log.info("区块大额交易监控vout操作异常，ending");
                  }
                }
              });
          
          // 插入
          if (((List<RawTransaction.Vout>) existListS[0]).size() != 0) {
            // 交易输出地址
            txElement.getVin().stream().parallel()
                .forEach(vinElement -> {
                  if (vinElement.getTxid() != null) {
                    try {
                      RawTransaction.Vout vinOut = BitcoindPoolUtil.getVout(vinElement.getTxid(), vinElement.getVout());
                      List<String> vinAddr = vinOut.getScriptPubKey().getAddresses();
                      if (vinAddr.size() != 0) {
                        String vinAddress = vinAddr.get(0);
                        ((List<String>) vinAddrListS[0]).add(vinAddress);
                      }
                    } catch (Exception e) {
                      e.printStackTrace();
                      log.info("获取交易输出地址异常");
                    }
                  }
                });
            for (RawTransaction.Vout vout : ((List<RawTransaction.Vout>) existListS[0])) {
              List<Integer> transIdList = transRuleList.stream()
                  .filter(s -> new BigDecimal(s.getMonitorMinVal()).compareTo(vout.getValue()) < 0)
                  .map(TransRule::getId)
                  .collect(Collectors.toList());
              transIdList.forEach(transId -> {
                MonitorTrans monitorTrans = new MonitorTrans();
                try {
                  monitorTrans
                      .setTransHash(txElement.getTxid())
                      .setUnusualCount(vout.getValue().toPlainString())
                      .setUnusualTime(LocalDateTime.ofEpochSecond(blockWithTransaction.getTime(), 0, ZoneOffset.ofHours(8)))
                      .setTransRuleId(transId)
                      .setToAddress(vout.getScriptPubKey().getAddresses().get(0))
                      .setFromAddress(StringUtils.join(((List<String>) vinAddrListS[0]).stream().collect(Collectors.toSet()), ","));
                } catch (Exception e) {
                  e.printStackTrace();
                }
                ((List<MonitorTrans>) monitorTransListS[0]).add(monitorTrans);
                
              });
            }
            ((List<String>) vinAddrListS[0]).clear();
            ((List<RawTransaction.Vout>) existListS[0]).clear();
            if (((List<MonitorTrans>) monitorTransListS[0]).size() != 0) {
              for (MonitorTrans monitorTrans : ((List<MonitorTrans>) monitorTransListS[0])) {
                int rows = monitorTransMapper.insert(monitorTrans);
                insertInspect(rows, null, monitorTrans.getTransHash(), monitorTrans);
              }
            }
            ((List<MonitorTrans>) monitorTransListS[0]).clear();
          }
        });
    log.info("区块大额交易监控ending");
  }
  
  private List<RawTransaction.Vout> addrAddUp(RawTransaction txElement) {
    List<RawTransaction.Vout> txVout = txElement.getVout().stream()
        .collect(Collectors.toMap(
            k -> {
              if (k.getScriptPubKey().getAddresses() != null) {
                return k.getScriptPubKey().getAddresses().get(0);
              }
              return null;
            }, v -> v, (o1, o2) -> {
              o1.setValue(o1.getValue().add(o2.getValue()));
              return o1;
            })).values().stream().collect(Collectors.toList());
    return txVout;
  }
  
  /**
   * 地址监控
   *
   * @param addrRuleList         地址监控规则列表
   * @param addrList             监控地址集合
   * @param blockWithTransaction 区块信息实例对象
   */
  public void addrMonitor(List<AddrRule> addrRuleList, List<String> addrList, BlockWithTransaction blockWithTransaction) {
    log.info("区块地址监控beginning");
    blockWithTransaction.getTx().stream().parallel()
        .forEach(txElement -> {
          // 输出地址去重累加
          List<RawTransaction.Vout> txVout = addrAddUp(txElement);
          txVout.stream().parallel()
              .forEach(voutElement -> {
                if (voutElement.getScriptPubKey().getAddresses() != null) {
                  try {
                    String addresses = voutElement.getScriptPubKey().getAddresses().get(0);
                    if (addresses != null) {
                      // 根据规则配配到的元素
                      String exist = null;
                      // 地址监控匹配
                      if (addrList.contains(addresses)) {
                        exist = addresses;
                      }
                      if (exist != null) {
                        String unusualCount = "+" + voutElement.getValue().toPlainString();
                        // 插入地址监控交易
                        addrMonitorInsert(addrRuleList, blockWithTransaction, txElement, exist, unusualCount);
                      }
                    }
                  } catch (Exception e) {
                    e.printStackTrace();
                    log.info("区块地址监控vout操作异常，ending");
                  }
                }
              });
          List<RawTransaction.Vout> txVinVout = new ArrayList<>();
          txElement.getVin().stream().parallel()
              .forEach(vinElement -> {
                if (vinElement.getTxid() != null) {
                  try {
                    RawTransaction.Vout vout = BitcoindPoolUtil.getVout(vinElement.getTxid(), vinElement.getVout());
                    txVinVout.add(vout);
                  } catch (Exception e) {
                    e.printStackTrace();
                  }
                }
              });
          txVinVout.stream().parallel()
              .forEach(voutElement -> {
                if (voutElement.getScriptPubKey().getAddresses() != null) {
                  try {
                    String addresses = voutElement.getScriptPubKey().getAddresses().get(0);
                    if (addresses != null) {
                      // 根据规则配配到的元素
                      String exist = null;
                      // 地址监控匹配
                      if (addrList.contains(addresses)) {
                        exist = addresses;
                      }
                      if (exist != null) {
                        String unusualCount = "-" + voutElement.getValue().toPlainString();
                        // 插入地址监控交易
                        addrMonitorInsert(addrRuleList, blockWithTransaction, txElement, exist, unusualCount);
                      }
                    }
                  } catch (Exception e) {
                    e.printStackTrace();
                    log.info("区块地址监控vout操作异常，ending");
                  }
                }
              });
        });
    log.info("区块地址监控ending");
  }
  
  private void addrMonitorInsert(List<AddrRule> addrRuleList, BlockWithTransaction blockWithTransaction, RawTransaction txElement, String
      exist, String unusualCount) {
    if (addrRuleList != null) {
      List<Integer> addrIdList = addrRuleList.stream()
          .filter(s -> s.getAddress().equals(exist))
          .map(AddrRule::getId)
          .collect(Collectors.toList());
      addrIdList.forEach(addrId -> {
        final MonitorAddr monitorAddr = new MonitorAddr();
        monitorAddr.setTransHash(txElement.getTxid())
            .setUnusualCount(unusualCount)
            .setUnusualTime(LocalDateTime.ofEpochSecond(blockWithTransaction.getTime(), 0, ZoneOffset.ofHours(8)))
            .setAddrRuleId(addrId);
        int rows = monitorAddrMapper.insert(monitorAddr);
        insertInspect(rows, monitorAddr, txElement.getTxid(), null);
      });
    }
  }
  
  /**
   * 检查、重试存入操作
   *
   * @param rows         存入操作返回的影响行数
   * @param monitorAddr  地址监控数据实例对象
   * @param transHash    匹配地址交易的交易哈希
   * @param monitorTrans 大额交易监控数据实例对象
   */
  public void insertInspect(
      int rows, MonitorAddr monitorAddr, String transHash, MonitorTrans monitorTrans) {
    if (rows == 0) {
      log.error("监控记录存入失败！交易哈希:" + transHash);
      // 数据库存入操作重试次数
      for (int i = 0; i < dataBaseRetryNum; i++) {
        if (rows == 0) {
          if (monitorAddr != null) {
            rows = monitorAddrMapper.insert(monitorAddr);
          }
          if (monitorTrans != null) {
            rows = monitorTransMapper.insert(monitorTrans);
          }
        } else {
          log.info("重试存入操作：第" + i + "次");
        }
      }
    }
    if (rows == 0) {
      log.error("监控记录重试存入失败！交易哈希:" + transHash);
    }
    log.info("监控记录存入成功！");
  }
}
