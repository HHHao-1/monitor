package com.chaindigg.monitor.userapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaindigg.monitor.common.dao.MonitorAddrMapper;
import com.chaindigg.monitor.common.entity.MonitorAddr;
import com.chaindigg.monitor.userapi.service.IMonitorAddrService;
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
public class MonitorAddrServiceImpl extends ServiceImpl<MonitorAddrMapper, MonitorAddr>
    implements IMonitorAddrService {

  @Override
  public Boolean add(MonitorAddr monitorAddr) {
    return this.save(monitorAddr);
  }
}
