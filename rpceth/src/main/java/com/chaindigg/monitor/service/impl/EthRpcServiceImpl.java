package com.chaindigg.monitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chaindigg.monitor.common.dao.*;
import com.chaindigg.monitor.common.entity.*;
import com.chaindigg.monitor.common.utils.DataBaseUtils;
import com.chaindigg.monitor.common.utils.StringUtils;
import com.chaindigg.monitor.service.IEthRpcService;
import com.chaindigg.monitor.utils.NoticeUtils;
import com.chaindigg.monitor.utils.RpcUtils;
import com.google.common.base.Joiner;
import com.zhifantech.bo.RawEthBlock;
import com.zhifantech.util.ParityPoolUtil;
import lombok.extern.slf4j.Slf4j;
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
import java.util.ArrayList;
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
  private RpcUtils rpcUtils;
  
  String transMailHtmlPath = this.getClass().getClassLoader().getResource("transMailHtml.txt").getPath();
  String addrMailHtmlPath = this.getClass().getClassLoader().getResource("addrMailHtml.txt").getPath();
  
  DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  
  public void ethMonitor() {
    try {
      monitor(rpcUtils.createQueryConditions("ETH")[0], rpcUtils.createQueryConditions("ETH")[1], "ETH");
    } catch (Exception e) {
      e.printStackTrace();
      log.info("ETH区块监控异常ending");
    }
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
      } catch (Exception e) {
        e.printStackTrace();
        log.info("获取区块高度异常");
      }
      if (!Objects.equals(maxBlockHeight, maxBlockHeightOld)) {
        log.info(coinKind + "区块监控beginning");
        maxBlockHeightOld = maxBlockHeight;
        RawEthBlock rawEthBlock = null;
        try {
          rawEthBlock = ParityPoolUtil.getBlockWithTransaction(maxBlockHeight);
        } catch (Exception e) {
          log.info("----------------------");
          e.printStackTrace();
          log.info("获取区块信息异常");
        }
        //      RawEthBlock rawEthBlock = ParityPoolUtil.getBlockWithTransaction(11384081L);
        //      log.info(rawEthBlock.toString());
        List<String> runList = Arrays.asList("addr", "trans");
        RawEthBlock finalRawEthBlock = rawEthBlock;
        runList.stream().parallel().forEach(s -> {
          switch (s) {
            case "addr":
              addrMonitor(addrRuleList, addrList, finalRawEthBlock, coinKind);
              break;
            case "trans":
              transMonitor(transRuleList, transValueList, finalRawEthBlock, coinKind);
              break;
          }
        });
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
          List<TransRule> matchRuleList = transRuleList.stream()
              .filter(y -> new BigDecimal(y.getMonitorMinVal()).compareTo(new BigDecimal(txElement.getValueRaw())) < 0)
              .collect(Collectors.toList());
          List<Integer> transIdList = transRuleList.stream()
              .filter(y -> new BigDecimal(y.getMonitorMinVal()).compareTo(new BigDecimal(txElement.getValueRaw())) < 0)
              .map(TransRule::getId)
              .collect(Collectors.toList());
          List<Integer> userIdList = matchRuleList.stream()
              .map(TransRule::getUserId)
              .collect(Collectors.toList());
          transIdList.forEach(transId -> {
            // region 通知数据
            String monitorKind = "大额交易";
            // 邮件
            String mailContent = null;
            try {
              mailContent = Joiner.on("").join(Files.lines(Paths.get(transMailHtmlPath)).collect(Collectors.toList()));
            } catch (IOException e) {
              e.printStackTrace();
            }
            String formatMail = StringUtils.templateString(
                mailContent,
                coinKind,
                monitorKind,
                txElement.getValueRaw(),
                txElement.getFrom(),
                txElement.getTo(),
                dtf.format(LocalDateTime.ofEpochSecond(blockWithTransaction.getTimestamp().longValue(), 0,
                    ZoneOffset.ofHours(8))),
                txElement.getHash());
            // 短信
            ArrayList<String> smsParams = new ArrayList<>();
            smsParams.add(coinKind);
            smsParams.add(txElement.getValueRaw());
            smsParams.add(txElement.getTo());
            smsParams.add(dtf.format(LocalDateTime.ofEpochSecond(blockWithTransaction.getTimestamp().longValue(), 0,
                ZoneOffset.ofHours(8))));
            // endregion
            // region 中间List
            QueryWrapper<User> userQuery = new QueryWrapper<>();
            userQuery.in("id", userIdList).eq("state", 1);
            List<User> userList = userMapper.selectList(userQuery);
            // endregion
            
            matchRuleList.forEach(addrRule -> {
              // region 通知
              User noticeUser =
                  userList.stream().filter(user -> user.getId().equals(addrRule.getUserId())).findFirst().get();
              try {
                NoticeUtils.notice(addrRule.getNoticeWay(), noticeUser.getPhone(), noticeUser.getEmail(),
                    monitorKind, transSmsTemplateCode, smsParams, formatMail);
              } catch (Exception e) {
                e.printStackTrace();
                log.info(coinKind + monitorKind + "监控通知失败，交易哈希：" + txElement.getHash());
              }
              // endregion
            });
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
          List<AddrRule> matchRuleList = addrRuleList.stream()
              .filter(s -> s.getAddress().equals(txElement.getFrom()))
              .collect(Collectors.toList());
          List<Integer> addrIdList = matchRuleList.stream()
              .map(AddrRule::getId)
              .collect(Collectors.toList());
          List<Integer> userIdList = matchRuleList.stream()
              .map(AddrRule::getUserId)
              .collect(Collectors.toList());
          addrIdList.forEach(addrId -> {
            // region 通知数据
            String monitorKind = "地址";
            // 邮件
            String mailContent = null;
            try {
              mailContent = Joiner.on("").join(Files.lines(Paths.get(addrMailHtmlPath)).collect(Collectors.toList()));
            } catch (IOException e) {
              e.printStackTrace();
            }
            String formatMail = StringUtils.templateString(
                mailContent,
                coinKind,
                monitorKind,
                "-" + txElement.getValueRaw(),
                txElement.getFrom(),
                dtf.format(LocalDateTime.ofEpochSecond(blockWithTransaction.getTimestamp().longValue(), 0,
                    ZoneOffset.ofHours(8))),
                txElement.getHash());
            // 短信
            ArrayList<String> smsParams = new ArrayList<>();
            smsParams.add(coinKind);
            smsParams.add("-" + txElement.getValueRaw());
            smsParams.add(txElement.getFrom());
            smsParams.add(dtf.format(LocalDateTime.ofEpochSecond(blockWithTransaction.getTimestamp().longValue(), 0,
                ZoneOffset.ofHours(8))));
            // endregion
            // region 中间List
            QueryWrapper<User> userQuery = new QueryWrapper<>();
            userQuery.in("id", userIdList).eq("state", 1);
            List<User> userList = userMapper.selectList(userQuery);
            // endregion
            
            matchRuleList.forEach(addrRule -> {
              // region 通知
              User noticeUser =
                  userList.stream().filter(user -> user.getId().equals(addrRule.getUserId())).findFirst().get();
              try {
                NoticeUtils.notice(addrRule.getNoticeWay(), noticeUser.getPhone(), noticeUser.getEmail(),
                    monitorKind, addrSmsTemplateCode, smsParams, formatMail);
              } catch (Exception e) {
                e.printStackTrace();
                log.info(coinKind + monitorKind + "监控通知失败，交易哈希：" + txElement.getHash());
              }
              // endregion
            });
            MonitorAddr monitorAddr = new MonitorAddr();
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
          List<AddrRule> matchRuleList = addrRuleList.stream()
              .filter(s -> s.getAddress().equals(txElement.getTo()))
              .collect(Collectors.toList());
          List<Integer> addrIdList = addrRuleList.stream()
              .filter(s -> s.getAddress().equals(txElement.getTo()))
              .map(AddrRule::getId)
              .collect(Collectors.toList());
          List<Integer> userIdList = matchRuleList.stream()
              .map(AddrRule::getUserId)
              .collect(Collectors.toList());
          addrIdList.forEach(addrId -> {
            // region 通知数据
            String monitorKind = "地址";
            // 邮件
            String mailContent = null;
            try {
              mailContent = Joiner.on("").join(Files.lines(Paths.get(addrMailHtmlPath)).collect(Collectors.toList()));
            } catch (IOException e) {
              e.printStackTrace();
            }
            String formatMail = StringUtils.templateString(
                mailContent,
                coinKind,
                monitorKind,
                "+" + txElement.getValueRaw(),
                txElement.getFrom(),
                dtf.format(LocalDateTime.ofEpochSecond(blockWithTransaction.getTimestamp().longValue(), 0,
                    ZoneOffset.ofHours(8))),
                txElement.getHash());
            // 短信
            ArrayList<String> smsParams = new ArrayList<>();
            smsParams.add(coinKind);
            smsParams.add("+" + txElement.getValueRaw());
            smsParams.add(txElement.getTo());
            smsParams.add(dtf.format(LocalDateTime.ofEpochSecond(blockWithTransaction.getTimestamp().longValue(), 0,
                ZoneOffset.ofHours(8))));
            // endregion
            // region 中间List
            QueryWrapper<User> userQuery = new QueryWrapper<>();
            userQuery.in("id", userIdList).eq("state", 1);
            List<User> userList = userMapper.selectList(userQuery);
            // endregion
            
            matchRuleList.forEach(addrRule -> {
              // region 通知
              User noticeUser =
                  userList.stream().filter(user -> user.getId().equals(addrRule.getUserId())).findFirst().get();
              try {
                NoticeUtils.notice(addrRule.getNoticeWay(), noticeUser.getPhone(), noticeUser.getEmail(),
                    monitorKind, transSmsTemplateCode, smsParams, formatMail);
              } catch (Exception e) {
                e.printStackTrace();
                log.info(coinKind + monitorKind + "监控通知失败，交易哈希：" + txElement.getHash());
              }
              // endregion
            });
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