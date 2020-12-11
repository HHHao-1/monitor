package com.chaindigg.monitor.userapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaindigg.monitor.userapi.dao.MonitorAddrVOMapper;
import com.chaindigg.monitor.userapi.service.IMonitorAddrVOService;
import com.chaindigg.monitor.userapi.vo.MonitorAddrVO;
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
  public List<MonitorAddrVO> selectByUserId(String id, Integer currentPage, Integer pageSize) {
    IPage<MonitorAddrVO> page = new Page<MonitorAddrVO>(currentPage, pageSize);
    QueryWrapper<MonitorAddrVO> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("b.user_id", id).orderByDesc("a.id");
    List<MonitorAddrVO> ll = monitorAddrVOMapper.selectByUserId(page, queryWrapper).getRecords();
    return monitorAddrVOMapper.selectByUserId(page, queryWrapper).getRecords();
  }
}
