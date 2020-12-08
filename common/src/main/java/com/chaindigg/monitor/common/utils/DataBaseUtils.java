package com.chaindigg.monitor.common.utils;

import com.chaindigg.monitor.common.dao.MonitorAddrMapper;
import com.chaindigg.monitor.common.dao.MonitorTransMapper;
import com.chaindigg.monitor.common.entity.MonitorAddr;
import com.chaindigg.monitor.common.entity.MonitorTrans;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.Resource;

@Slf4j
@PropertySource(value = {"classpath:rpc.properties"})
public class DataBaseUtils {
  
  @Value("${database-retry-num}") // insert重试次数
  private static int dataBaseRetryNum;
  @Resource
  private static MonitorAddrMapper monitorAddrMapper;
  @Resource
  private static MonitorTransMapper monitorTransMapper;
  
  /**
   * 检查、重试存入操作
   *
   * @param rows         存入操作返回的影响行数
   * @param monitorAddr  地址监控数据实例对象
   * @param transHash    匹配地址交易的交易哈希
   * @param monitorTrans 大额交易监控数据实例对象
   */
  public static void insertInspect(
      int rows, MonitorAddr monitorAddr, String transHash, MonitorTrans monitorTrans, String coinKind) {
    if (rows == 0) {
      if (monitorAddr != null) {
        log.error(coinKind + "地址监控记录存入失败！交易哈希:" + transHash);
        // 数据库存入操作重试
        for (int i = 0; i < dataBaseRetryNum; i++) {
          if (rows == 0) {
            rows = monitorAddrMapper.insert(monitorAddr);
          } else {
            log.info(coinKind + "地址监控记录重试存入操作：第" + i + "次");
          }
        }
      }
      if (monitorTrans != null) {
        log.error(coinKind + "大额交易监控记录存入失败！交易哈希:" + transHash);
        // 数据库存入操作重试
        for (int i = 0; i < dataBaseRetryNum; i++) {
          if (rows == 0) {
            rows = monitorTransMapper.insert(monitorTrans);
          } else {
            log.info(coinKind + "大额交易监控记录重试存入操作：第" + i + "次");
          }
        }
      }
    }
    if (rows == 0) {
      if (monitorAddr != null) {
        log.error(coinKind + "地址监控记录重试存入失败！交易哈希:" + transHash);
        return;
      }
      if (monitorTrans != null) {
        log.error(coinKind + "大额交易监控记录重试存入失败！交易哈希:" + transHash);
        return;
      }
    }
    if (monitorAddr != null) {
      log.info(coinKind + "地址监控记录存入成功！");
    }
    if (monitorTrans != null) {
      log.info(coinKind + "大额交易监控记录存入成功！");
    }
  }
}
