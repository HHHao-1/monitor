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

import java.util.HashMap;
import java.util.Map;

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
  public Map<String, Object> selectByUserId(String id, Integer currentPage, Integer pageSize) {
    IPage<MonitorAddrVO> page = new Page<MonitorAddrVO>(currentPage, pageSize);
    QueryWrapper<MonitorAddrVO> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("b.user_id", id).eq("b.state", 1).orderByDesc("a.id");
    IPage<MonitorAddrVO> res = monitorAddrVOMapper.selectByUserId(page, queryWrapper);
    Map<String, Object> map = new HashMap<>();
    map.put("total", res.getTotal());
    map.put("data", res.getRecords());
    return map;
  }
  
  @Override
  public Map<String, Object> selectByCoinKind(String id, Integer currentPage, Integer pageSize, String[] coninKinds) {
    IPage<MonitorAddrVO> page = new Page<MonitorAddrVO>(currentPage, pageSize);
    QueryWrapper<MonitorAddrVO> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("b.user_id", id).eq("b.state", 1).orderByDesc("a.id");
    queryWrapper.in("b.coin_kind", coninKinds);
    IPage<MonitorAddrVO> res = monitorAddrVOMapper.selectByUserId(page, queryWrapper);
    Map<String, Object> map = new HashMap<>();
    map.put("total", res.getTotal());
    map.put("data", res.getRecords());
    return map;
  }
  
  @Override
  public Map<String, Object> selectByEvent(String id, Integer currentPage, Integer pageSize, String eventName) {
    IPage<MonitorAddrVO> page = new Page<MonitorAddrVO>(currentPage, pageSize);
    QueryWrapper<MonitorAddrVO> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("b.user_id", id).eq("b.state", 1).orderByDesc("a.id");
    queryWrapper.in("b.event_name", eventName);
    IPage<MonitorAddrVO> res = monitorAddrVOMapper.selectByUserId(page, queryWrapper);
    Map<String, Object> map = new HashMap<>();
    map.put("total", res.getTotal());
    map.put("data", res.getRecords());
    return map;
  }
  
  @Override
  public Map<String, Object> selectByMark(String id, Integer currentPage, Integer pageSize, String mark) {
    IPage<MonitorAddrVO> page = new Page<MonitorAddrVO>(currentPage, pageSize);
    QueryWrapper<MonitorAddrVO> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("b.user_id", id).eq("b.state", 1).orderByDesc("a.id");
    queryWrapper.in("b.address_mark", mark);
    IPage<MonitorAddrVO> res = monitorAddrVOMapper.selectByUserId(page, queryWrapper);
    Map<String, Object> map = new HashMap<>();
    map.put("total", res.getTotal());
    map.put("data", res.getRecords());
    return map;
  }
}
