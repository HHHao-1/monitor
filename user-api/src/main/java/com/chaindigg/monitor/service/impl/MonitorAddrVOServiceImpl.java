package com.chaindigg.monitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaindigg.monitor.dao.MonitorAddrVOMapper;
import com.chaindigg.monitor.service.IMonitorAddrVOService;
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
public class MonitorAddrVOServiceImpl extends ServiceImpl<MonitorAddrVOMapper, MonitorAddrVO>
    implements IMonitorAddrVOService {

  private final MonitorAddrVOMapper monitorAddrVOMapper;

  @Override
  public List<MonitorAddrVO> selectByUserId(String id, int currentPage, int pageSize) {
    IPage<MonitorAddrVO> page = new Page<MonitorAddrVO>(currentPage, pageSize);
    QueryWrapper<MonitorAddrVO> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("b.id", id).orderByDesc("a.id");
    return monitorAddrVOMapper.selectByUserId(page, queryWrapper).getRecords();
  }

  //  @Override
  //  public List<MonitorAddrVO> selectAll(int currentPage, int pageSize) {
  //    IPage<MonitorAddrVO> page = new Page<MonitorAddrVO>(currentPage, pageSize);
  //    QueryWrapper<MonitorAddrVO> queryWrapper = new QueryWrapper<>();
  //    queryWrapper.orderByDesc("a.id");
  //    return monitorAddrVOMapper.selectAll(page, queryWrapper).getRecords();
  //  }
}
