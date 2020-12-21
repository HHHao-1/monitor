package com.chaindigg.monitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chaindigg.monitor.common.dao.*;
import com.chaindigg.monitor.common.entity.*;
import com.chaindigg.monitor.common.utils.DataBaseUtils;
import com.chaindigg.monitor.service.IBitCommonService;
import com.chaindigg.monitor.utils.NoticeUtils;
import com.google.common.base.Joiner;
import com.sulacosoft.bitcoindconnector4j.response.BlockWithTransaction;
import com.sulacosoft.bitcoindconnector4j.response.RawTransaction;
import com.zhifantech.util.BitcoindPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@PropertySource(value = {"classpath:config.properties"})
public class BitCommonServiceImpl implements IBitCommonService {
  
  @Value("${database-retry-num}") // insert重试次数
  private int dataBaseRetryNum;
  @Value("${addr-monitor-templateCode}")
  String addrSmsTemplateCode;
  @Value("${trans-monitor-templateCode}")
  String transSmsTemplateCode;
  
  @Resource
  private AddrRuleMapper addrRuleMapper;
  @Resource
  private TransRuleMapper transRuleMapper;
  @Resource
  private MonitorAddrMapper monitorAddrMapper;
  @Resource
  private MonitorTransMapper monitorTransMapper;
  @Resource
  private UserMapper userMapper;
  @Resource
  private NoticeUtils noticeUtils;
  
  String transMailHtmlPath = this.getClass().getClassLoader().getResource("transMailHtml.txt").getPath();
  String addrMailHtmlPath = this.getClass().getClassLoader().getResource("addrMailHtml.txt").getPath();
  
  DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  
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
        maxBlockHeight = BitcoindPoolUtil.getMaxBlockHeight();
      } catch (Exception e) {
        e.printStackTrace();
        log.info("获取区块高度异常");
      }
      if (!Objects.equals(maxBlockHeight, maxBlockHeightOld)) {
        log.info(coinKind + "区块监控beginning");
        maxBlockHeightOld = maxBlockHeight;
        BlockWithTransaction blockWithTransaction = null;
        try {
          blockWithTransaction = BitcoindPoolUtil.getBlock(maxBlockHeight);
        } catch (Exception e) {
          e.printStackTrace();
          log.info("获取区块信息异常");
        }
        //      RawEthBlock rawEthBlock = ParityPoolUtil.getBlockWithTransaction(11384081L);
        //      log.info(rawEthBlock.toString());
        List<String> runList = Arrays.asList("addr", "trans");
        BlockWithTransaction finalBlockWithTransaction = blockWithTransaction;
        runList.stream().parallel().forEach(s -> {
          switch (s) {
            case "addr":
              addrMonitor(addrRuleList, addrList, finalBlockWithTransaction, coinKind);
              break;
            case "trans":
              transMonitor(transRuleList, transValueList, finalBlockWithTransaction, coinKind);
              break;
          }
        });
      }
    }
  }
  
  /**
   * 地址监控
   *
   * @param addrRuleList         地址监控规则列表
   * @param addrList             监控地址集合
   * @param blockWithTransaction 区块信息实例对象
   */
  public void addrMonitor(List<AddrRule> addrRuleList, List<String> addrList,
                          BlockWithTransaction blockWithTransaction, String coinKind) {
    log.info(coinKind + "区块地址监控beginning");
    String monitorKind = "地址";
    blockWithTransaction.getTx().stream()
        .forEach(txElement -> {
          // 输出地址去重累加
          List<RawTransaction.Vout> txVout = addrAddUp(txElement.getVout());
          txVout.stream().filter(s -> s.getScriptPubKey().getAddresses() != null)
              .filter(x -> {
                for (AddrRule y : addrRuleList) {
                  if (Objects.equals(y.getAddress(), x.getScriptPubKey().getAddresses().get(0))) {
                    if (y.getMonitorMinVal() == null) {
                      return true;
                    } else if (new BigDecimal(y.getMonitorMinVal()).compareTo(x.getValue()) < 0) {
                      return true;
                    }
                  }
                }
                return false;
              })
              .forEach(voutElement -> {
                try {
                  // 根据规则配配到的元素
                  String exist = voutElement.getScriptPubKey().getAddresses().get(0);
                  // 地址监控匹配
                  if (exist != null) {
                    String unusualCount = "+" + voutElement.getValue().toPlainString();
                    // region 通知数据
                    // 邮件
                    String mailContent = null;
                    try {
                      mailContent = Joiner.on("").join(Files.lines(Paths.get(addrMailHtmlPath)).collect(Collectors.toList()));
                    } catch (IOException e) {
                      e.printStackTrace();
                    }
                    String formatMail = com.chaindigg.monitor.common.utils.StringUtils.templateString(
                        mailContent,
                        coinKind,
                        monitorKind,
                        unusualCount,
                        exist,
                        dtf.format(LocalDateTime.ofEpochSecond(blockWithTransaction.getTime(), 0,
                            ZoneOffset.ofHours(8))),
                        txElement.getTxid());
                    // 短信
                    ArrayList<String> smsParams = new ArrayList<>();
                    smsParams.add(coinKind);
                    smsParams.add(exist);
                    smsParams.add(unusualCount);
                    smsParams.add(dtf.format(LocalDateTime.ofEpochSecond(blockWithTransaction.getTime(), 0,
                        ZoneOffset.ofHours(8))));
                    // endregion
                    String finalFormatMail = formatMail;
                    String finalExist = exist;
                    // region 中间List
                    List<AddrRule> matchRuleList = addrRuleList.stream()
                        .filter(s -> s.getAddress().equals(finalExist))
                        .collect(Collectors.toList());
                    List<Integer> transIdList = matchRuleList.stream()
                        .map(AddrRule::getId)
                        .collect(Collectors.toList());
                    List<Integer> userIdList = matchRuleList.stream()
                        .map(AddrRule::getUserId)
                        .collect(Collectors.toList());
                    QueryWrapper<User> userQuery = new QueryWrapper<>();
                    userQuery.in("id", userIdList).eq("state", 1);
                    List<User> userList = userMapper.selectList(userQuery);
                    // endregion
                    matchRuleList.forEach(addrRule -> {
                      // region 通知
                      User noticeUser =
                          userList.stream().filter(user -> user.getId().equals(addrRule.getUserId())).findFirst().get();
                      try {
                        noticeUtils.notice(addrRule.getNoticeWay(), noticeUser.getPhone(), noticeUser.getEmail(),
                            monitorKind, transSmsTemplateCode, smsParams, finalFormatMail, coinKind,
                            unusualCount, txElement.getTxid());
                      } catch (Exception e) {
                        e.printStackTrace();
                        log.info(coinKind + monitorKind + "监控通知失败，交易哈希：" + txElement.getTxid());
                      }
                      // endregion
                    });
                    // 插入地址监控交易
                    addrMonitorInsert(addrRuleList, blockWithTransaction, txElement, exist, unusualCount, coinKind);
                  }
                } catch (Exception e) {
                  e.printStackTrace();
                  log.info(coinKind + "区块地址监控vout操作异常，ending");
                }
              });
          List<RawTransaction.Vout> txVinVout = new ArrayList<>();
          RawTransaction finalTxElement = txElement;
          finalTxElement.getVin()
              .forEach(s -> {
                if (s.getTxid() != null) {
                  try {
                    RawTransaction.Vout vout = BitcoindPoolUtil.getVout(s.getTxid(), s.getVout());
                    txVinVout.add(vout);
                  } catch (Exception e) {
                    e.printStackTrace();
                  }
                }
              });
          txVinVout.stream().filter(s -> s.getScriptPubKey().getAddresses() != null)
              .filter(x -> {
                for (AddrRule y : addrRuleList) {
                  if (Objects.equals(y.getAddress(), x.getScriptPubKey().getAddresses().get(0))) {
                    return true;
                  } else if (new BigDecimal(y.getMonitorMinVal()).compareTo(x.getValue()) < 0) {
                    return true;
                  }
                }
                return false;
              })
              .forEach(voutElement -> {
                try {
                  String exist = voutElement.getScriptPubKey().getAddresses().get(0);
                  if (exist != null) {
                    String unusualCount = "-" + voutElement.getValue().toPlainString();
                    // region 通知数据
                    // 邮件
                    String mailContent = null;
                    try {
                      mailContent = Joiner.on("").join(Files.lines(Paths.get(addrMailHtmlPath)).collect(Collectors.toList()));
                    } catch (IOException e) {
                      e.printStackTrace();
                    }
                    String formatMail = com.chaindigg.monitor.common.utils.StringUtils.templateString(
                        mailContent,
                        coinKind,
                        monitorKind,
                        unusualCount,
                        exist,
                        LocalDateTime.ofEpochSecond(blockWithTransaction.getTime(), 0,
                            ZoneOffset.ofHours(8)).toString(),
                        txElement.getTxid());
                    // 短信
                    ArrayList<String> smsParams = new ArrayList<>();
                    smsParams.add(coinKind);
                    smsParams.add(exist);
                    smsParams.add(unusualCount);
                    smsParams.add(LocalDateTime.ofEpochSecond(blockWithTransaction.getTime(), 0,
                        ZoneOffset.ofHours(8)).toString());
                    // endregion
                    
                    // 插入地址监控交易
                    addrMonitorInsert(addrRuleList, blockWithTransaction, txElement, exist, unusualCount, coinKind);
                  }
                } catch (Exception e) {
                  e.printStackTrace();
                  log.info(coinKind + "区块地址监控vout操作异常，ending");
                }
              });
        });
    log.info(coinKind + "区块地址监控ending");
  }
  
  /**
   * 大额交易监控
   *
   * @param transRuleList        大额交易监控表
   * @param transValueList       监控额度集合
   * @param blockWithTransaction 区块信息实例对象
   */
  public void transMonitor(List<TransRule> transRuleList, List<String> transValueList,
                           BlockWithTransaction blockWithTransaction, String coinKind) {
    log.info(coinKind + "区块大额交易监控beginning");
    
    // region 变量声明
    List<String> vinAddrList = new ArrayList<>();
    List<String> voutAddrList = new ArrayList<>();
    List<MonitorTrans> monitorTransList = new ArrayList<>();
    List<RawTransaction.Vout> existList = new ArrayList<>();
    // 解决lambda表达式中外部声明的变量必须final的问题
    Object[] vinAddrListS = {vinAddrList};
    Object[] voutAddrListS = {voutAddrList};
    Object[] monitorTransListS = {monitorTransList};
    Object[] existListS = {existList};
    // endregion
    
    blockWithTransaction.getTx().stream()
        .forEach(txElement -> {
          //输入地址列表
          List<RawTransaction.Vout> txVinList = new ArrayList<>();
          txElement.getVin().stream()
              .filter(txvi -> Objects.nonNull(txvi.getTxid()) && Objects.nonNull(txvi.getVout()))
              .forEach(txvi -> {
                try {
                  RawTransaction.Vout vinOut = BitcoindPoolUtil.getVout(txvi.getTxid(), txvi.getVout());
                  txVinList.add(vinOut);
                } catch (Exception e) {
                  e.printStackTrace();
                }
              });
          // 输出地址累加去重
          List<RawTransaction.Vout> txVout = addrAddUp(txElement.getVout());
          // 输出地址遍历匹配
          txVout.stream().filter(s -> s.getScriptPubKey().getAddresses() != null)
              .filter(txvo -> transValueList.stream().map(BigDecimal::new).filter(x -> x.compareTo(txvo.getValue()) < 0).count() > 0)
              .forEach(voutElement -> {
                try {
                  String exist = voutElement.getScriptPubKey().getAddresses().get(0);
                  if (exist != null) {
                    // 中间List
                    List<TransRule> matchRuleList = transRuleList.stream()
                        .filter(s -> new BigDecimal(s.getMonitorMinVal()).compareTo(voutElement.getValue()) < 0)
                        .collect(Collectors.toList());
                    
                    List<Integer> transIdList = matchRuleList.stream()
                        .map(TransRule::getId)
                        .collect(Collectors.toList());
                    
                    List<Integer> userIdList = matchRuleList.stream()
                        .map(TransRule::getUserId)
                        .collect(Collectors.toList());
                    
                    QueryWrapper<User> userQuery = new QueryWrapper<>();
                    userQuery.in("id", userIdList).eq("state", 1);
                    List<User> userList = userMapper.selectList(userQuery);
                    // 判断匹配地址是否是找零地址
                    if (!vinAddrList.contains(exist)) {
                      ((List<RawTransaction.Vout>) existListS[0]).add(voutElement);
                      // 变量：交易时间
                      LocalDateTime transTime = LocalDateTime.ofEpochSecond(blockWithTransaction.getTime(), 0,
                          ZoneOffset.ofHours(8));
                      // 判断输入地址是否为一个
                      String fromAddr = null;
                      if (txElement.getVin().size() <= 1) {
                        RawTransaction.Vout vinOut = null;
                        vinOut = BitcoindPoolUtil.getVout(txElement.getVin().get(0).getTxid(),
                            txElement.getVin().get(0).getVout());
                        Optional.ofNullable(vinOut.getScriptPubKey().getAddresses()).ifPresent(a -> vinAddrList.add(a.get(0)));
                      } else {
                        txVinList.stream().forEach(s -> Optional.ofNullable(s.getScriptPubKey().getAddresses()).ifPresent(x -> vinAddrList.add(x.get(0))));
                      }
                      fromAddr =
                          StringUtils.join(((List<String>) vinAddrListS[0]).stream().collect(Collectors.toSet()), ",");
                      // region 通知数据处理
                      // 邮件
                      String mailContent = null;
                      String monitorKind = "大额交易";
                      try {
                        mailContent =
                            Joiner.on("").join(Files.lines(Paths.get(transMailHtmlPath)).collect(Collectors.toList()));
                      } catch (IOException e) {
                        e.printStackTrace();
                      }
                      String formatMail = null;
                      String value = Optional.ofNullable(voutElement.getValue().toPlainString()).orElse(null);
                      formatMail = com.chaindigg.monitor.common.utils.StringUtils.templateString(
                          mailContent,
                          coinKind,
                          monitorKind,
                          value,
                          fromAddr,
                          exist,
                          dtf.format(transTime),
                          Optional.ofNullable(txElement.getTxid()).orElse(null));
                      // 短信
                      ArrayList<String> smsParams = new ArrayList<>();
                      smsParams.add(coinKind);
                      smsParams.add(voutElement.getValue().toPlainString());
                      smsParams.add(exist);
                      smsParams.add(dtf.format(transTime));
                      // endregion
                      String finalFromAddr = fromAddr;
                      String finalFormatMail = formatMail;
                      matchRuleList.forEach(transRule -> {
                        // region 通知
                        User noticeUser =
                            userList.stream().filter(user -> user.getId().equals(transRule.getUserId())).findFirst().orElse(null);
                        try {
                          noticeUtils.notice(transRule.getNoticeWay(), noticeUser.getPhone(), noticeUser.getEmail(),
                              monitorKind, transSmsTemplateCode, smsParams, finalFormatMail, coinKind,
                              voutElement.getValue().toPlainString(), txElement.getTxid());
                        } catch (Exception e) {
                          e.printStackTrace();
                          log.info(coinKind + monitorKind + "监控通知失败，交易哈希：" + txElement.getTxid());
                        }
                        // endregion
                        MonitorTrans monitorTrans = new MonitorTrans();
                        monitorTrans
                            .setTransHash(txElement.getTxid())
                            .setUnusualCount(voutElement.getValue().toPlainString())
                            .setUnusualTime(transTime)
                            .setTransRuleId(transRule.getId())
                            .setToAddress(exist)
                            .setFromAddress(finalFromAddr)
                            .setNoticeTime(LocalDateTime.now());
                        int rows = monitorTransMapper.insert(monitorTrans);
                        DataBaseUtils.insertInspect(rows, null, monitorTrans.getTransHash(), monitorTrans, coinKind);
                      });
                    }
                  }
                } catch (Exception e) {
                  e.printStackTrace();
                  log.info(coinKind + "区块大额交易监控vout操作异常，ending");
                }
              });
          // 输入地址累加去重
          List<RawTransaction.Vout> txVin = addrAddUp(txVinList);
          txVin.stream().filter(s -> s.getScriptPubKey().getAddresses() != null)
              .filter(txvi -> transValueList.stream().map(BigDecimal::new).filter(x -> x.compareTo(txvi.getValue()) < 0).count() > 0)
              .forEach(vinElement -> {
                try {
                  String exist = vinElement.getScriptPubKey().getAddresses().get(0);
                  if (exist != null) {
                    // 中间List
                    List<TransRule> matchRuleList = transRuleList.stream()
                        .filter(s -> new BigDecimal(s.getMonitorMinVal()).compareTo(vinElement.getValue()) < 0)
                        .collect(Collectors.toList());
                    
                    List<Integer> transIdList = matchRuleList.stream()
                        .map(TransRule::getId)
                        .collect(Collectors.toList());
                    
                    List<Integer> userIdList = matchRuleList.stream()
                        .map(TransRule::getUserId)
                        .collect(Collectors.toList());
                    
                    QueryWrapper<User> userQuery = new QueryWrapper<>();
                    userQuery.in("id", userIdList).eq("state", 1);
                    List<User> userList = userMapper.selectList(userQuery);
                    
                    ((List<RawTransaction.Vout>) existListS[0]).add(vinElement);
                    // 变量：交易时间
                    LocalDateTime transTime = LocalDateTime.ofEpochSecond(blockWithTransaction.getTime(), 0,
                        ZoneOffset.ofHours(8));
                    // 判断输出地址是否为一个
                    String toAddr = null;
                    if (txElement.getVout().size() <= 1) {
                      RawTransaction.Vout vinOut = BitcoindPoolUtil.getVout(txElement.getVin().get(0).getTxid(),
                          txElement.getVin().get(0).getVout());
                      Optional.ofNullable(txElement.getVout().get(0).getScriptPubKey().getAddresses()).ifPresent(a -> voutAddrList.add(a.get(0)));
                    } else {
                      txElement.getVout().stream().forEach(s -> Optional.ofNullable(s.getScriptPubKey().getAddresses()).ifPresent(x -> voutAddrList.add(x.get(0))));
                    }
                    toAddr =
                        StringUtils.join(((List<String>) voutAddrListS[0]).stream().collect(Collectors.toSet()), ",");
                    // region 通知数据处理
                    // 邮件
                    String mailContent = null;
                    String monitorKind = "大额交易";
                    try {
                      mailContent =
                          Joiner.on("").join(Files.lines(Paths.get(transMailHtmlPath)).collect(Collectors.toList()));
                    } catch (IOException e) {
                      e.printStackTrace();
                    }
                    String formatMail = null;
                    formatMail = com.chaindigg.monitor.common.utils.StringUtils.templateString(
                        mailContent,
                        coinKind,
                        monitorKind,
                        Optional.ofNullable(vinElement.getValue().toPlainString()).orElse(null),
                        exist,
                        toAddr,
                        dtf.format(transTime),
                        Optional.ofNullable(txElement.getTxid()).orElse(null));
                    // 短信
                    ArrayList<String> smsParams = new ArrayList<>();
                    smsParams.add(coinKind);
                    smsParams.add(vinElement.getValue().toPlainString());
                    smsParams.add(exist);
                    smsParams.add(dtf.format(transTime));
                    // endregion
                    String finalFormatMail = formatMail;
                    String finalToAddr = toAddr;
                    matchRuleList.forEach(transRule -> {
                      // region 通知
                      User noticeUser =
                          userList.stream().filter(user -> user.getId().equals(transRule.getUserId())).findFirst().get();
                      try {
                        noticeUtils.notice(transRule.getNoticeWay(), noticeUser.getPhone(), noticeUser.getEmail(),
                            monitorKind, transSmsTemplateCode, smsParams, finalFormatMail, coinKind,
                            vinElement.getValue().toPlainString(), txElement.getTxid());
                      } catch (Exception e) {
                        e.printStackTrace();
                        log.info(coinKind + monitorKind + "监控通知失败，交易哈希：" + txElement.getTxid());
                      }
                      // endregion
                      MonitorTrans monitorTrans = new MonitorTrans();
                      monitorTrans
                          .setTransHash(txElement.getTxid())
                          .setUnusualCount(vinElement.getValue().toPlainString())
                          .setUnusualTime(transTime)
                          .setTransRuleId(transRule.getId())
                          .setToAddress(finalToAddr)
                          .setFromAddress(exist)
                          .setNoticeTime(LocalDateTime.now());
                      int rows = monitorTransMapper.insert(monitorTrans);
                      DataBaseUtils.insertInspect(rows, null, monitorTrans.getTransHash(), monitorTrans, coinKind);
                    });
                  }
                } catch (Exception e) {
                  e.printStackTrace();
                  log.info(coinKind + "区块大额交易监控vout操作异常，ending");
                }
              });
        });
    log.info(coinKind + "区块大额交易监控ending");
  }
  
  /**
   * 接收地址累加去重
   *
   * @param txElement 交易元素
   * @return 返回累加去重后的交易vout
   */
  private List<RawTransaction.Vout> addrAddUp(List<RawTransaction.Vout> voutList) {
    List<RawTransaction.Vout> txVout = voutList.stream()
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
  private void addrMonitorInsert(List<AddrRule> addrRuleList, BlockWithTransaction
      blockWithTransaction, RawTransaction txElement, String
                                     exist, String unusualCount, String coinKind) {
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
            .setAddrRuleId(addrId)
            .setNoticeTime(LocalDateTime.now());
        int rows = monitorAddrMapper.insert(monitorAddr);
        DataBaseUtils.insertInspect(rows, monitorAddr, txElement.getTxid(), null, coinKind);
      });
    }
  }
}
