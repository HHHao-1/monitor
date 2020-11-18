package com.chaindigg.monitor_java.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaindigg.monitor_java.dao.MonitorAddrMapper;
import com.chaindigg.monitor_java.dto.MonitorAddrDTO;
import com.chaindigg.monitor_java.po.MonitorAddr;
import com.chaindigg.monitor_java.service.IMonitorAddrService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chenghao
 * @since 2020-11-17
 */
@Service
@RequiredArgsConstructor
public class MonitorAddrServiceImpl extends ServiceImpl<MonitorAddrMapper, MonitorAddr> implements IMonitorAddrService {

  private final MonitorAddrMapper monitorAddrMapper;

  @Override
  public List<MonitorAddrDTO> selectByUserId(String id) {
    return monitorAddrMapper.selectByUserId(id);
  }

  @Override
  public List<MonitorAddrDTO> selectAll() {
    return monitorAddrMapper.selectAll();
  }
}
