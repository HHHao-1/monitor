package com.chaindigg.monitor.userapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaindigg.monitor.common.entity.MonitorTrans;

/**
 * 服务类
 *
 * @author chenghao
 * @since 2020-11-17
 */
public interface IMonitorTransService extends IService<MonitorTrans> {
  Boolean add(MonitorTrans monitorAddr);
}
