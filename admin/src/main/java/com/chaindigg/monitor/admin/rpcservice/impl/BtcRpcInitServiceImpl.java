package com.chaindigg.monitor.admin.rpcservice.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chaindigg.monitor.admin.rpcservice.IBtcRpcInitService;
import com.chaindigg.monitor.admin.utils.DataBaseUtils;
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
import com.zhifantech.util.BitcoindPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@PropertySource(value = {"classpath:rpc.properties"})
public class BtcRpcInitServiceImpl implements IBtcRpcInitService {
  
  @Value("${database-retry-num}") // insert重试次数
  private int dataBaseRetryNum;
  @Value("#{'${btc-rpc-urls}'.split(',')}")
  private List<String> urlList; // 节点url
  @Value("${btc-retry-num}")
  private int rpcRetryNum; // 链接失败重试次数
  @Value("${btc-retry-interv}")
  private int rpcRetryInterv; // 重新链接间隔时间
  @Value("${btc-user-name}")
  private String username; // 用户名
  @Value("${btc-password}")
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
      BitcoindPoolUtil.init(urlList, username, password, rpcRetryNum, rpcRetryInterv);
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
      addrQueryWrapper.eq("coin_kind", "BTC");
      QueryWrapper transQueryWrapper = new QueryWrapper();
      transQueryWrapper.select("id", "monitor_min_val").eq("state", 1);
      transQueryWrapper.eq("coin_kind", "BTC");
      List<AddrRule> addrRuleList = addrRuleMapper.selectList(addrQueryWrapper);
      List<TransRule> transRuleList = transRuleMapper.selectList(transQueryWrapper);
      List<String> addrList = addrRuleList.stream().map(AddrRule::getAddress).collect(Collectors.toList());
      List<String> transValueList = transRuleList.stream().map(TransRule::getMonitorMinVal).collect(Collectors.toList());
      // endregion
      
      Long maxBlockHeight = null;
      while (true) {
        Long maxBlockHeightOld = maxBlockHeight;
        maxBlockHeight = BitcoindPoolUtil.getMaxBlockHeight();
        if (!Objects.equals(maxBlockHeight, maxBlockHeightOld)) {
          BlockWithTransaction blockWithTransaction = null;
          try {
            blockWithTransaction = BitcoindPoolUtil.getBlock(maxBlockHeight);
          } catch (Exception e) {
            e.printStackTrace();
          }
//        BlockWithTransaction blockWithTransaction = BitcoindPoolUtil.getBlock(659314);
          List<String> runList = new ArrayList();
          runList.add("addr");
          runList.add("trans");
          BlockWithTransaction finalBlockWithTransaction = blockWithTransaction;
          runList.stream().parallel().forEach(s -> {
            switch (s) {
              case "addr":
                log.info("线程：" + Thread.currentThread().getName());
                addrMonitor(addrRuleList, addrList, finalBlockWithTransaction);
                break;
              case "trans":
                log.info("线程：" + Thread.currentThread().getName());
                transMonitor(transRuleList, transValueList, finalBlockWithTransaction);
                break;
            }
          });
        }
      }
//      MailService mailService = new MailServiceImpl();
//      mailService.init(null);
//      mailService.sendHtmlMail("454947233@qq.com", "test", "test");
//
//      SmsService smsService = new SmsServiceImpl();
//      smsService.init(null);
//      Integer templateCode = TencentSmsTemplateEnum.SMS_MONITORING.getCode();
//      ArrayList<String> objects = new ArrayList<>();
//      objects.add("1234");
//      objects.add("3");
//      objects.add("3");
//      objects.add("3");
//      objects.add("3");
//      objects.add("3");
//      smsService.sendSms(null, "13279202134", templateCode, objects);
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
    List<MonitorTrans> monitorTransList = new ArrayList<>();
    List<RawTransaction.Vout> existList = new ArrayList<>();
    // 解决lambda表达式中外部声明的变量必须final的问题
    Object[] vinAddrListS = {vinAddrList};
    Object[] monitorTransListS = {monitorTransList};
    Object[] existListS = {existList};
    blockWithTransaction.getTx().stream().parallel()
        .forEach(txElement -> {
          // 输出地址累加去重
          List<RawTransaction.Vout> txVout = addrAddUp(txElement);
          // 输出地址遍历匹配
          txVout.stream()
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
          // 规则匹配信息存入数据库
          if (((List<RawTransaction.Vout>) existListS[0]).size() != 0) {
            // 交易输出地址
            txElement.getVin().stream()
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
            // 存入数据库
            if (((List<MonitorTrans>) monitorTransListS[0]).size() != 0) {
              for (MonitorTrans monitorTrans : ((List<MonitorTrans>) monitorTransListS[0])) {
                int rows = monitorTransMapper.insert(monitorTrans);
                DataBaseUtils.insertInspect(rows, null, monitorTrans.getTransHash(), monitorTrans);
              }
            }
            ((List<MonitorTrans>) monitorTransListS[0]).clear();
          }
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
          // 输出地址去重累加
          List<RawTransaction.Vout> txVout = addrAddUp(txElement);
          txVout.stream()
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
          txElement.getVin().stream()
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
          txVinVout.stream()
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
  
  /**
   * 接收地址累加去重
   *
   * @param txElement 交易元素
   * @return 返回累加去重后的交易vout
   */
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
   * 地址监控存入操作
   *
   * @param addrRuleList         地址规则列表
   * @param blockWithTransaction 区块信息
   * @param txElement            交易信息
   * @param exist                匹配到的地址
   * @param unusualCount         异动金额
   */
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
        DataBaseUtils.insertInspect(rows, monitorAddr, txElement.getTxid(), null);
      });
    }
  }
}
