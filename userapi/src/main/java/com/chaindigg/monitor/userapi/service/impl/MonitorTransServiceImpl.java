package com.chaindigg.monitor.userapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaindigg.monitor.common.entity.MonitorTrans;
import com.chaindigg.monitor.userapi.dao.MonitorTransMapper;
import com.chaindigg.monitor.userapi.service.IMonitorTransService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 服务实现类
 *
 * @author chenghao
 * @since 2020-11-17
 */
@Service
@RequiredArgsConstructor
public class MonitorTransServiceImpl extends ServiceImpl<MonitorTransMapper, MonitorTrans>
    implements IMonitorTransService {

  private final MonitorTransMapper monitorTransMapper;

  @Override
  public Boolean add(MonitorTrans monitorTrans) {
    return this.save(monitorTrans);
  }
}
