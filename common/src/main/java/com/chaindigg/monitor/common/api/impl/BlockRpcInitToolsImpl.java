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
import com.sulacosoft.bitcoindconnector4j.BitcoindApi;
import com.sulacosoft.bitcoindconnector4j.response.BlockWithTransaction;
import com.sulacosoft.bitcoindconnector4j.response.RawTransaction;
import com.zhifantech.strategy.SeqRetryStrategy;
import com.zhifantech.util.BitcoindPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@PropertySource(value = {"classpath:rpc.properties"})
public class BlockRpcInitToolsImpl implements IBlockRpcInitTools {

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

    private SeqRetryStrategy seqRetryStrategy;

    private Class clazz = BitcoindApi.class;

    public void monitor() {
        try {
            // 查询规则
            QueryWrapper addrQueryWrapper = new QueryWrapper();
            addrQueryWrapper.select("id", "address");
            QueryWrapper transQueryWrapper = new QueryWrapper();
            transQueryWrapper.select("id", "monitor_min_val");
            List<AddrRule> addrRuleList = addrRuleMapper.selectList(addrQueryWrapper);
            List<TransRule> transRuleList = transRuleMapper.selectList(transQueryWrapper);
            List<String> addrList = addrRuleList.stream().map(AddrRule::getAddress).collect(Collectors.toList());
            List<String> transList = transRuleList.stream().map(TransRule::getMonitorMinVal).collect(Collectors.toList());
            //      Long maxBlockHeight;
            //      while (true) {
            //      Long maxBlockHeightOld = maxBlockHeight;
            //      maxBlockHeight = BitcoindPoolUtil.getMaxBlockHeight();
            //      if (!Objects.equals(maxBlockHeight, maxBlockHeightOld)) {
            //        BlockWithTransaction blockWithTransaction =
            // BitcoindPoolUtil.getBlock(maxBlockHeight);
            BlockWithTransaction blockWithTransaction = BitcoindPoolUtil.getBlock(659314);
            addrmonitor(addrRuleList, addrList, blockWithTransaction);

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
     * 地址监控
     *
     * @param addrRuleList         地址监控规则列表
     * @param addrList             监控地址集合
     * @param blockWithTransaction 区块信息实例对象
     */
    public void addrmonitor(List<AddrRule> addrRuleList, List<String> addrList, BlockWithTransaction blockWithTransaction) {
        log.info("区块地址监控beginning");
        blockWithTransaction.getTx().stream().parallel()
                .forEach(txElement -> {
                    txElement.getVout().stream()
                            .forEach(voutElement -> {
                                if (voutElement.getScriptPubKey().getAddresses() != null) {
                                    try {
                                        List<String> addresses = voutElement.getScriptPubKey().getAddresses().stream()
                                                .collect(Collectors.toList());
                                        insertMonitorData(txElement, voutElement, addrRuleList, addrList, addresses, null,
                                                null, null, null, txElement.getTime(), true);
//                                        insertMonitorData(
//                                                addrRuleList,
//                                                addrList,
//                                                txElement,
//                                                voutElement,
//                                                addresses,
//                                                txElement.getBlocktime(),
//                                                true);
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
                                        List<String> addresses = vout.getScriptPubKey().getAddresses().stream()
                                                .collect(Collectors.toList());
                                        insertMonitorData(txElement, vout, addrRuleList, addrList, addresses, null,
                                                null, null, null, txElement.getBlocktime(), false);
//                                        insertMonitorAddr(
//                                                addrRuleList,
//                                                addrList,
//                                                txElement,
//                                                vout,
//                                                addresses,
//                                                txElement.getBlocktime(),
//                                                false);
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
                                  List<String> voutAddresses, List<TransRule> transRuleList, List<String> transValueList, String vinValue,
                                  String voutValue, long blocktime, Boolean inOrOut) {

        // 根据规则配配到的元素
        List<String> exist = null;

        // 地址监控匹配
        if (addrRuleList != null) {
            exist = voutAddresses.stream()
                    .filter(addr -> addrList.contains(addr) && (addr != null))
                    .collect(Collectors.toList());
        }

        // 大额交易匹配
        if (transRuleList != null) {

            // 扫描交易vout
            if (voutValue != null) {
                exist = transValueList.stream()
                        .filter(value -> Integer.parseInt(voutValue) >= Integer.parseInt(value))
                        .collect(Collectors.toList());
            }
            // 扫描交易vin
            if (vinValue != null) {
                exist = transValueList.stream()
                        .filter(value -> Integer.parseInt(vinValue) >= Integer.parseInt(value))
                        .collect(Collectors.toList());
            }
        }

        // 匹配到元素时的操作
        if (exist.size() != 0) {
            String unusualCount;

            // 判断交易金额正负
            if (inOrOut) {
                unusualCount = "+" + vout.getValue().toPlainString();
            } else {
                unusualCount = "-" + vout.getValue().toPlainString();
            }
            exist.forEach(existElement -> {

                // 插入地址监控交易
                if (addrRuleList != null) {
                    List<Integer> addrIdList = addrRuleList.stream()
                            .filter(s -> s.getAddress().equals(existElement))
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
                    Method method = null;
                    RawTransaction rawTransaction = null;
                    try {
                        method = clazz.getMethod("getrawtransaction", String.class, int.class);
                        Object object = seqRetryStrategy.apply(method, txElement.getTxid(), 2);
                        rawTransaction = (RawTransaction) object;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    List<Integer> transIdList = transRuleList.stream()
                            .filter(s -> s.getMonitorMinVal().equals(existElement))
                            .map(TransRule::getId)
                            .collect(Collectors.toList());
                    RawTransaction finalRawTransaction = rawTransaction;
                    transIdList.forEach(transId -> {
                        MonitorTrans monitorTrans = new MonitorTrans();
                        monitorTrans.setTransHash(txElement.getTxid())
                                .setUnusualCount(unusualCount)
                                .setUnusualTime(LocalDateTime.ofEpochSecond(blocktime, 0, ZoneOffset.ofHours(8)))
                                .setTransRuleId(transId);
                        List<String> list = vout.getScriptPubKey().getAddresses();

                        // 大额交易监控，扫描交易vout
                        if (voutValue != null) {
                            String toAddress = null;
                            if (list.size() != 0) {
                                toAddress = list.get(0);
                            }
                            String vinAddress = StringUtils.join(finalRawTransaction.getVin(), ",");
                            monitorTrans.setToAddress(toAddress).setFromAddress(vinAddress);
                        }

                        // 大额交易监控，扫描交易vin
                        if (vinValue != null) {
                            String fromAddress = null;
                            if (list.size() != 0) {
                                fromAddress = list.get(0);
                            }
                            String voutAddress = StringUtils.join(finalRawTransaction.getVout(), ",");
                            monitorTrans.setToAddress(voutAddress).setFromAddress(fromAddress);
                        }
                        int rows = monitorTransMapper.insert(monitorTrans);
                        insertInspect(rows, null, txElement.getTxid(), monitorTrans);
                    });
                }
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
                    log.info("地址监控重试存入操作：第" + i + "次");
                }
            }
        }
        if (rows == 0) {
            log.error("地址监控记录存失败！交易哈希:" + transHash);
        }
        log.info("地址监控记录存入成功！");
    }
}
