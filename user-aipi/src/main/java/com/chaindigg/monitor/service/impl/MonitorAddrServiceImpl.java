package com.chaindigg.monitor.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaindigg.monitor.dao.MonitorAddrMapper;
import com.chaindigg.monitor.entity.TransRule;
import com.chaindigg.monitor.vo.MonitorAddrVO;
import com.chaindigg.monitor.entity.MonitorAddr;
import com.chaindigg.monitor.service.IMonitorAddrService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
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
  public List<MonitorAddrVO> selectByUserId(String id, int currentPage, int pageSize) {
    IPage<TransRule> page = new Page<TransRule>(currentPage, pageSize);
    return monitorAddrMapper.selectByUserId(page, id);
  }

  @Override
  public List<MonitorAddrVO> selectAll(int currentPage, int pageSize) {
    IPage<TransRule> page = new Page<TransRule>(currentPage, pageSize);
    return monitorAddrMapper.selectAll(page);
  }
}
