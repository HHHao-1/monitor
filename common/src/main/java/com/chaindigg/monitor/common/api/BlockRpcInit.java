package com.chaindigg.monitor.common.api;

import com.chaindigg.monitor.common.dao.AddrRuleMapper;
import com.chaindigg.monitor.common.dao.MonitorAddrMapper;
import com.chaindigg.monitor.common.dao.MonitorTransMapper;
import com.chaindigg.monitor.common.dao.TransRuleMapper;
import com.zhifantech.util.BitcoindPoolUtil;
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
@PropertySource(value = {"classpath:rpc.properties"})
public class BlockRpcInit implements ApplicationRunner {

    @Value("#{'${rpc-urls}'.split(',')}")
    private List<String> rpcUrls; // 节点url

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
    @Resource
    private IBlockRpcInitTools blockRpcInitTools;

    @Override
    public void run(ApplicationArguments args) {
//        try {
        BitcoindPoolUtil.init(rpcUrls, username, password, rpcRetryNum, rpcRetryInterv);
        blockRpcInitTools.monitor();
        // 查询规则
//            QueryWrapper addrQueryWrapper = new QueryWrapper();
//            addrQueryWrapper.select("id", "address");
//            QueryWrapper transQueryWrapper = new QueryWrapper();
//            transQueryWrapper.select("id", "monitor_min_val");
//            List<AddrRule> addrRuleList = addrRuleMapper.selectList(addrQueryWrapper);
//            List<TransRule> transRuleList = transRuleMapper.selectList(transQueryWrapper);
//            List<String> addrList =
//                    addrRuleList.stream().map(AddrRule::getAddress).collect(Collectors.toList());
//            List<String> transList =
//                    transRuleList.stream().map(TransRule::getMonitorMinVal).collect(Collectors.toList());
//
//            Long maxBlockHeight;
//            //      while (true) {
//            //      Long maxBlockHeightOld = maxBlockHeight;
//            //      maxBlockHeight = BitcoindPoolUtil.getMaxBlockHeight();
//            //      if (!Objects.equals(maxBlockHeight, maxBlockHeightOld)) {
//            //        BlockWithTransaction blockWithTransaction =
//            // BitcoindPoolUtil.getBlock(maxBlockHeight);
//            BlockWithTransaction blockWithTransaction = BitcoindPoolUtil.getBlock(659314);
//            addrmonitor(addrRuleList, addrList, blockWithTransaction);
//
//            //      }
//            //      }
//            //      Thread.sleep(3000);
//            //      }
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.info("区块监控异常，ending");
//        }
    }
}

//    /**
//     * 大额交易
//     *
//     * @param transRuleList        大额交易监控规则列表
//     * @param transList            打个交易监控集合
//     * @param blockWithTransaction 区块信息实例对象
//     */
//    private void tansmonitor(
//            List<TransRule> transRuleList,
//            List<String> transList,
//            BlockWithTransaction blockWithTransaction) {
//        log.info("区块大额交易监控beginning");
//        blockWithTransaction.getTx().stream()
//                .parallel()
//                .forEach(
//                        txElement -> {
//                            txElement.getVout().stream()
//                                    .forEach(
//                                            voutElement -> {
//                                                if (voutElement.getScriptPubKey().getAddresses() != null) {
//                                                    try {
//                                                        String value = voutElement.getValue().toPlainString();
//                                                        insertMonitorTrans(
//                                                                transRuleList,
//                                                                transList,
//                                                                txElement,
//                                                                voutElement,
//                                                                value,
//                                                                txElement.getBlocktime(),
//                                                                true);
//                                                    } catch (Exception e) {
//                                                        e.printStackTrace();
//                                                        log.info("区块地址监控vout操作异常，ending");
//                                                    }
//                                                }
//                                            });
//                            txElement.getVin().stream()
//                                    .forEach(
//                                            vinElement -> {
//                                                if (vinElement.getTxid() != null) {
//                                                    try {
//                                                        RawTransaction.Vout vout =
//                                                                BitcoindPoolUtil.getVout(
//                                                                        vinElement.getTxid(), vinElement.getVout());
//                                                        List<String> addresses =
//                                                                vout.getScriptPubKey().getAddresses().stream()
//                                                                        .collect(Collectors.toList());
//                                                        insertMonitorAddr(
//                                                                addrRuleList,
//                                                                addrSet,
//                                                                txElement,
//                                                                vout,
//                                                                addresses,
//                                                                txElement.getBlocktime(),
//                                                                false);
//                                                    } catch (Exception e) {
//                                                        e.printStackTrace();
//                                                        log.info("区块地址监控vin操作异常，ending");
//                                                    }
//                                                }
//                                            });
//                        });
//        log.info("区块地址监控ending");
//    }
//
//    /**
//     * 地址监控
//     *
//     * @param addrRuleList         地址监控规则列表
//     * @param addrList             地址监控集合
//     * @param blockWithTransaction 区块信息实例对象
//     */
//    private void addrmonitor(List<AddrRule> addrRuleList, List<String> addrList, BlockWithTransaction blockWithTransaction) {
//        log.info("区块地址监控beginning");
//        blockWithTransaction.getTx().stream()
//                .parallel()
//                .forEach(
//                        txElement -> {
//                            txElement.getVout().stream()
//                                    .forEach(
//                                            voutElement -> {
//                                                if (voutElement.getScriptPubKey().getAddresses() != null) {
//                                                    try {
//                                                        List<String> addresses =
//                                                                voutElement.getScriptPubKey().getAddresses().stream()
//                                                                        .collect(Collectors.toList());
//                                                        insertMonitorData(
//                                                                addrRuleList,
//                                                                addrList,
//                                                                txElement,
//                                                                voutElement,
//                                                                addresses,
//                                                                txElement.getBlocktime(),
//                                                                true);
//                                                    } catch (Exception e) {
//                                                        e.printStackTrace();
//                                                        log.info("区块地址监控vout操作异常，ending");
//                                                    }
//                                                }
//                                            });
//                            txElement.getVin().stream()
//                                    .forEach(
//                                            vinElement -> {
//                                                if (vinElement.getTxid() != null) {
//                                                    try {
//                                                        RawTransaction.Vout vout =
//                                                                BitcoindPoolUtil.getVout(
//                                                                        vinElement.getTxid(), vinElement.getVout());
//                                                        List<String> addresses =
//                                                                vout.getScriptPubKey().getAddresses().stream()
//                                                                        .collect(Collectors.toList());
//                                                        insertMonitorAddr(
//                                                                addrRuleList,
//                                                                addrList,
//                                                                txElement,
//                                                                vout,
//                                                                addresses,
//                                                                txElement.getBlocktime(),
//                                                                false);
//                                                    } catch (Exception e) {
//                                                        e.printStackTrace();
//                                                        log.info("区块地址监控vin操作异常，ending");
//                                                    }
//                                                }
//                                            });
//                        });
//        log.info("区块地址监控ending");
//    }
//
//  /**
//   * 匹配监控规则，插入监控记录
//   *
//   * @param addrRuleList 地址监控规则列表
//   * @param transList 地址监控集合
//   * @param txElement 每笔交易元素
//   * @param vout 交易输出对象
//   * @param addresses 交易输出地址集合
//   * @param blocktime 交易确认时间
//   * @param inOrOut true:金额加，false:金额减
//   * @return
//   */
//  private void insertMonitorTrans(
//      List<TransRule> transRuleList,
//      List<String> transList,
//      RawTransaction txElement,
//      RawTransaction.Vout vout,
//      String values,
//      long blocktime,
//      Boolean inOrOut) {
//
//    List<String> existTransValue =
//        transList.stream()
//            .filter(transValue -> transValue.equals(values))
//            .collect(Collectors.toList());
//
//    if (existTransValue.size() != 0) {
//      String unusualCount;
//      if (inOrOut) {
//        unusualCount = "+" + vout.getValue().toPlainString();
//      } else {
//        unusualCount = "-" + vout.getValue().toPlainString();
//      }
//      existTransValue.forEach(
//          existTx -> {
//            List<Integer> transIdList =
//                transRuleList.stream()
//                    .filter(s -> s.getMonitorMinVal().equals(existTransValue))
//                    .map(TransRule::getId)
//                    .collect(Collectors.toList());
//            transIdList.forEach(
//                transId -> {
//                  final MonitorTrans monitorTrans = new MonitorTrans();
//                  monitorTrans
//                      .setTransHash(txElement.getTxid())
//                      .setUnusualCount(unusualCount)
//                      .setUnusualTime(
//                          LocalDateTime.ofInstant(
//                              Instant.ofEpochMilli(blocktime), ZoneId.systemDefault()))
//                      .setTransRuleId(transId)
//                      .setToAddress(vout.getScriptPubKey().getAddresses().get(0))
//                      .setFromAddress();
//                  int rows = monitorTransMapper.insert(monitorTrans);
//                  insertInspect(rows, null, txElement.getTxid());
//                });
//          });
//    }
//  }
//
//  /**
//   * 匹配监控规则，插入监控记录
//   *
//   * @param addrRuleList 地址监控规则列表
//   * @param addrList 地址监控集合
//   * @param txElement 每笔交易元素
//   * @param vout 交易输出对象
//   * @param addresses 交易输出地址集合
//   * @param blocktime 交易确认时间
//   * @param inOrOut true:金额加，false:金额减
//   * @return
//   */
//  private void insertMonitorAddr(
//      List<AddrRule> addrRuleList,
//      List<String> addrList,
//      RawTransaction txElement,
//      RawTransaction.Vout vout,
//      List<String> addresses,
//      long blocktime,
//      Boolean inOrOut) {
//    // vout地址集合与监控地址集合取交集
//    List<String> existAddresses =
//        addresses.stream()
//            .filter(addr -> addrList.contains(addr) && (addr != null))
//            .collect(Collectors.toList());
//    if (existAddresses.size() != 0) {
//      String unusualCount;
//      if (inOrOut) {
//        unusualCount = "+" + vout.getValue().toPlainString();
//      } else {
//        unusualCount = "-" + vout.getValue().toPlainString();
//      }
//      existAddresses.forEach(
//          existAddr -> {
//            List<Integer> addrIdList =
//                addrRuleList.stream()
//                    .filter(s -> s.getAddress().equals(existAddr))
//                    .map(AddrRule::getId)
//                    .collect(Collectors.toList());
//            addrIdList.forEach(
//                addrId -> {
//                  final MonitorAddr monitorAddr = new MonitorAddr();
//                  monitorAddr
//                      .setTransHash(txElement.getTxid())
//                      .setUnusualCount(unusualCount)
//                      .setUnusualTime(
//                          LocalDateTime.ofInstant(
//                              Instant.ofEpochMilli(blocktime), ZoneId.systemDefault()))
//                      .setAddrRuleId(addrId);
//                  int rows = monitorAddrMapper.insert(monitorAddr);
//                  insertInspect(rows, monitorAddr, txElement.getTxid());
//                });
//          });
//    }
//  }
//
//  /**
//   * 检查、重试存入操作
//   *
//   * @param rows 存入操作返回的影响行数
//   * @param monitorAddr 地址监控记录实例对象
//   * @param transHash 匹配地址交易的交易哈希
//   */
//  private void insertInspect(int rows, MonitorAddr monitorAddr, String transHash) {
//    if (rows == 0) {
//      // 数据库存入操作重试次数
//      for (int i = 0; i < dataBaseRetryNum; i++) {
//        if (rows == 0) {
//          rows = monitorAddrMapper.insert(monitorAddr);
//        } else {
//          log.info("地址监控重试存入操作：第" + i + "次");
//        }
//      }
//    }
//    if (rows == 0) {
//      log.error("地址监控记录存失败！交易哈希:" + transHash);
//    }
//    log.info("地址监控记录存入成功！");
//  }
//}
