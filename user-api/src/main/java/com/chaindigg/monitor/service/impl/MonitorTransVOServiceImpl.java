package com.chaindigg.monitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaindigg.monitor.dao.MonitorTransVOMapper;
import com.chaindigg.monitor.service.IMonitorTransVOService;
import com.chaindigg.monitor.vo.MonitorTransVO;
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
public class MonitorTransVOServiceImpl extends ServiceImpl<MonitorTransVOMapper, MonitorTransVO>
    implements IMonitorTransVOService {

  private final MonitorTransVOMapper monitorTransVOMapper;

  @Override
  public List<MonitorTransVO> selectByUserId(String id, int currentPage, int pageSize) {
    IPage<MonitorTransVO> page = new Page<MonitorTransVO>(currentPage, pageSize);
    QueryWrapper<MonitorTransVO> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("b.id", id).orderByDesc("a,id");
    return monitorTransVOMapper.selectByUserId(page, queryWrapper).getRecords();
  }

  //  @Override
  //  public List<MonitorTransVO> selectAll(int currentPage, int pageSize) {
  //    IPage<MonitorTransVO> page = new Page<MonitorTransVO>(currentPage, pageSize);
  //    QueryWrapper<MonitorTransVO> queryWrapper = new QueryWrapper<>();
  //    queryWrapper.orderByDesc("a.id");
  //    return monitorTransVOMapper.selectAll(page, queryWrapper).getRecords();
  //  }
}
