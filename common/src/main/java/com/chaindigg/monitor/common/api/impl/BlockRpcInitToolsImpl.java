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
//            addrMonitor(addrRuleList, addrList, blockWithTransaction);
      transMonitor(transRuleList, transValueList, blockWithTransaction);
      //      }
      //      }
      //      Thread.sleep(3000);
      //      }
    } catch (Exception e) {
      e.printStackTrace();
      log.info("区块监控异常，ending");
    }
  }
  
  public void transMonitor(List<TransRule> transRuleList, List<String> transValueList, BlockWithTransaction blockWithTransaction) {
    log.info("区块大额交易监控beginning");
    blockWithTransaction.getTx().stream().parallel()
        .forEach(txElement -> {
          txElement.getVout().stream()
              .forEach(voutElement -> {
                if (voutElement.getValue() != null) {
                  try {
                    String voutValue = voutElement.getValue().toPlainString();
                    insertMonitorData(txElement, voutElement, null, null, null, transRuleList,
                        transValueList, null, voutValue, blockWithTransaction.getTime(), true);
                  } catch (Exception e) {
                    e.printStackTrace();
                    log.info("区块大额交易监控vout操作异常，ending");
                  }
                }
              });
//          txElement.getVin().stream()
//              .forEach(vinElement -> {
//                if (vinElement.getTxid() != null) {
//                  try {
//                    RawTransaction.Vout vout = BitcoindPoolUtil.getVout(vinElement.getTxid(), vinElement.getVout());
//                    String vinValue = vout.getValue().toPlainString();
//                    insertMonitorData(txElement, vout, null, null, null, transRuleList,
//                        transValueList, vinValue, null, blockWithTransaction.getTime(), false);
//                  } catch (Exception e) {
//                    e.printStackTrace();
//                    log.info("区块大额交易监控vin操作异常，ending");
//                  }
//                }
//              });
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
  public void addrMonitor(List<AddrRule> addrRuleList, List<String> addrList, BlockWithTransaction blockWithTransaction) {
    log.info("区块地址监控beginning");
    blockWithTransaction.getTx().stream().parallel()
        .forEach(txElement -> {
          txElement.getVout().stream()
              .forEach(voutElement -> {
                if (voutElement.getScriptPubKey().getAddresses() != null) {
                  try {
                    String addresses = voutElement.getScriptPubKey().getAddresses().get(0);
                    if (addresses != null) {
                      insertMonitorData(txElement, voutElement, addrRuleList, addrList, addresses, null,
                          null, null, null, blockWithTransaction.getTime(), true);
                    }
                  } catch (Exception e) {
                    e.printStackTrace();
                    log.info("区块地址监控vout操作异常，ending");
                  }
                }
              });
          txElement.getVin().stream()
              .forEach(vinElement -> {
                if (vinElement.getTxid() != null) {
                  try {
                    RawTransaction.Vout vout = BitcoindPoolUtil.getVout(vinElement.getTxid(), vinElement.getVout());
                    String addresses = vout.getScriptPubKey().getAddresses().get(0);
                    if (addresses != null) {
                      insertMonitorData(txElement, vout, addrRuleList, addrList, addresses, null,
                          null, null, null, blockWithTransaction.getTime(), false);
                    }
                  } catch (Exception e) {
                    e.printStackTrace();
                    log.info("区块地址监控vin操作异常，ending");
                  }
                }
              });
        });
    log.info("区块地址监控ending");
  }
  
  /**
   * 匹配监控规则，匹配到元素，则保存监控数据
   *
   * @param txElement      交易实例
   * @param vout           交易输出实例
   * @param addrRuleList   地址监控规则列表
   * @param addrList       监控地址列表
   * @param voutAddresses  交易输出地址列表
   * @param transRuleList  打个交易监控规则列表
   * @param transValueList 监控额度列表
   * @param vinValue       交易输入额度
   * @param voutValue      交易输出额度
   * @param blocktime      交易时间
   * @param inOrOut        交易金额正负；true:金额加，false:金额减
   */
  public void insertMonitorData(RawTransaction txElement, RawTransaction.Vout vout, List<AddrRule> addrRuleList, List<String> addrList,
                                String voutAddresses, List<TransRule> transRuleList, List<String> transValueList, String vinValue,
                                String voutValue, long blocktime, Boolean inOrOut) {
    
    // 根据规则配配到的元素
    String exist = null;
    
    // 地址监控匹配
    if (addrRuleList != null) {
      if (addrList.contains(voutAddresses)) {
        exist = voutAddresses;
      }
//          voutAddresses.stream()
//          .filter(addr -> addrList.contains(addr) && (addr != null))
//          .collect(Collectors.toList());
    }
    
    // 大额交易匹配
    if (transRuleList != null) {
      
      // 扫描交易vout
      if (voutValue != null) {
        List<String> list = transValueList.stream()
            .filter(s -> new BigDecimal(s).compareTo(new BigDecimal(voutValue)) < 0)
            .collect(Collectors.toList());
        if (list.size() != 0) {
          try {
            exist = vout.getScriptPubKey().getAddresses().get(0);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
//        exist = transValueList.stream()
//            .filter(value -> new BigDecimal(voutValue).compareTo(new BigDecimal(value)) > 0)
//            .collect(Collectors.toList());
      }
      // 扫描交易vin
      if (vinValue != null) {
        //TODO
//        exist = transValueList.stream()
//            .filter(value -> new BigDecimal(vinValue).compareTo(new BigDecimal(value)) > 0)
//            .collect(Collectors.toList());
      }
    }
    
    // 匹配到元素时的操作
//    if (exist.size() != 0) {
    if (exist != null) {
      String unusualCount;
      
      // 判断交易金额正负
      if (inOrOut) {
        unusualCount = "+" + vout.getValue().toPlainString();
      } else {
        unusualCount = "-" + vout.getValue().toPlainString();
      }

//      exist.forEach(existElement -> {
      
      // 插入地址监控交易
      String finalExist = exist;
      if (addrRuleList != null) {
        List<Integer> addrIdList = addrRuleList.stream()
            .filter(s -> s.getAddress().equals(finalExist))
            .map(AddrRule::getId)
            .collect(Collectors.toList());
        addrIdList.forEach(addrId -> {
          final MonitorAddr monitorAddr = new MonitorAddr();
          monitorAddr.setTransHash(txElement.getTxid())
              .setUnusualCount(unusualCount)
              .setUnusualTime(LocalDateTime.ofEpochSecond(blocktime, 0, ZoneOffset.ofHours(8)))
              .setAddrRuleId(addrId);
          int rows = monitorAddrMapper.insert(monitorAddr);
          insertInspect(rows, monitorAddr, txElement.getTxid(), null);
        });
      }
      
      // 插入大额交易监控
      if (transRuleList != null) {
        // 交易输出地址
        List<String> vinAddrList = new ArrayList<>();
        txElement.getVin().stream().parallel()
            .forEach(vinElement -> {
              if (vinElement.getTxid() != null) {
                try {
                  RawTransaction.Vout vinOut = BitcoindPoolUtil.getVout(vinElement.getTxid(), vinElement.getVout());
                  List<String> vinAddr = vinOut.getScriptPubKey().getAddresses();
                  if (vinAddr.size() != 0) {
                    String vinAddress = vinOut.getScriptPubKey().getAddresses().get(0);
                    vinAddrList.add(vinAddress);
                  }
                } catch (Exception e) {
                  e.printStackTrace();
                  log.info("获取交易输出地址异常");
                }
              }
            });
        // 判断匹配地址是否是找零地址
        if (!vinAddrList.contains(exist)) {
          // 匹配地址去除找零地址
//          List<String> existNoRollBback =
//              finalExist.stream().filter(s -> !s.equals(vout.getScriptPubKey().getAddresses().get(0))).collect(Collectors.toList());
          // 匹配地址重复相加


//        List<String> copylist = voutAddrPending;
//        for (int i = 0; i < voutAddrPending.size(); i++) {
//          copylist.remove(i);
//          int finalI = i;
//          copylist.stream().filter(s -> s.equals(voutAddrPending.get(finalI))).reduce(0, )
//        }

//          Method method = null;
//          RawTransaction rawTransaction = null;
//          try {
//            method = clazz.getMethod("getBitcoindApi");
//            Object object = seqRetryStrategy.apply(method);
//            BitcoindApi bitcoindApi = (BitcoindApi) object;
//            rawTransaction = bitcoindApi.getrawtransaction(txElement.getTxid(), 2);
//          } catch (Exception e) {
//            e.printStackTrace();
//          }
          List<Integer> transIdList = transRuleList.stream()
              .filter(s -> new BigDecimal(s.getMonitorMinVal()).compareTo(new BigDecimal(voutValue)) < 0)
              .map(TransRule::getId)
              .collect(Collectors.toList());
//          RawTransaction finalRawTransaction = rawTransaction;
          String finalExist1 = exist;
          transIdList.forEach(transId -> {
            MonitorTrans monitorTrans = new MonitorTrans();
            monitorTrans.setTransHash(txElement.getTxid())
                .setUnusualCount(unusualCount)
                .setUnusualTime(LocalDateTime.ofEpochSecond(blocktime, 0, ZoneOffset.ofHours(8)))
                .setTransRuleId(transId)
                .setToAddress(finalExist1)
                .setFromAddress(StringUtils.join(vinAddrList, ","));
//            List<String> list = vout.getScriptPubKey().getAddresses();
//
//            // 大额交易监控，扫描交易vout
//            if (voutValue != null) {
//              String toAddress = null;
//              if (list.size() != 0) {
//                toAddress = list.get(0);
//              }
////              try {
////                RawTransaction.Vout vout1 = BitcoindPoolUtil.getVout(txElement.getVin().get(0).getTxid(), txElement.getVin().get(0).getVout());
////              } catch (Exception e) {
////                e.printStackTrace();
////              }
//              List<String> addresses = vout.getScriptPubKey().getAddresses().stream()
//                  .collect(Collectors.toList());
//              String vinAddress = StringUtils.join(finalRawTransaction.getVin(), ",");
//              monitorTrans.setToAddress(toAddress).setFromAddress(vinAddress);
//            }
//
//            // 大额交易监控，扫描交易vin
//            if (vinValue != null) {
//              String fromAddress = null;
//              if (list.size() != 0) {
//                fromAddress = list.get(0);
//              }
//              String voutAddress = StringUtils.join(finalRawTransaction.getVout(), ",");
//              monitorTrans.setToAddress(voutAddress).setFromAddress(fromAddress);
//            }
            int rows = monitorTransMapper.insert(monitorTrans);
            insertInspect(rows, null, txElement.getTxid(), monitorTrans);
          });
        }
      }
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
