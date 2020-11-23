package com.chaindigg.monitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaindigg.monitor.dao.MonitorAddrMapper;
import com.chaindigg.monitor.entity.MonitorAddr;
import com.chaindigg.monitor.service.IMonitorAddrService;
import com.chaindigg.monitor.vo.MonitorAddrVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

  private final MonitorAddrMapper monitorAddrMapper;

  @Override
  public List<MonitorAddrVO> selectByUserId(String id, int currentPage, int pageSize) {
    IPage<MonitorAddrVO> page = new Page<MonitorAddrVO>(currentPage, pageSize);
    QueryWrapper<MonitorAddrVO> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("b.id", id);
    return monitorAddrMapper.selectByUserId(page, queryWrapper).getRecords();
  }

  @Override
  public List<MonitorAddrVO> selectAll(int currentPage, int pageSize) {
    IPage<MonitorAddrVO> page = new Page<MonitorAddrVO>(currentPage, pageSize);
    QueryWrapper<MonitorAddrVO> queryWrapper = new QueryWrapper<>();
    queryWrapper.orderByDesc("a.id");
    return monitorAddrMapper.selectAll(page, queryWrapper).getRecords();
  }
}
