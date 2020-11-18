package com.chaindigg.monitor_java.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaindigg.monitor_java.dao.MonitorTransMapper;
import com.chaindigg.monitor_java.dto.MonitorTransDTO;
import com.chaindigg.monitor_java.po.MonitorTrans;
import com.chaindigg.monitor_java.service.IMonitorTransService;
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
public class MonitorTransServiceImpl extends ServiceImpl<MonitorTransMapper, MonitorTrans> implements IMonitorTransService {

  private final MonitorTransMapper monitorTransMapper;


  @Override
  public List<MonitorTransDTO> selectByUserId(String id) {
    return monitorTransMapper.selectByUserId(id);
  }

  @Override
  public List<MonitorTransDTO> selectAll() {
    return  monitorTransMapper.selectAll();
  }
}
