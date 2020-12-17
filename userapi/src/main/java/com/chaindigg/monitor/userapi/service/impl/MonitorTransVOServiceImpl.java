package com.chaindigg.monitor.userapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaindigg.monitor.userapi.dao.MonitorTransVOMapper;
import com.chaindigg.monitor.userapi.service.IMonitorTransVOService;
import com.chaindigg.monitor.userapi.vo.MonitorTransVO;
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
public class MonitorTransVOServiceImpl extends ServiceImpl<MonitorTransVOMapper, MonitorTransVO>
    implements IMonitorTransVOService {
  
  private final MonitorTransVOMapper monitorTransVOMapper;
  
  @Override
  public Map<String, Object> selectByUserId(String id, Integer currentPage, Integer pageSize) {
    IPage<MonitorTransVO> page = new Page<MonitorTransVO>(currentPage, pageSize);
    QueryWrapper<MonitorTransVO> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("b.user_id", id).eq("b.state", 1).orderByDesc("a.id");
    IPage<MonitorTransVO> res = monitorTransVOMapper.selectByUserId(page, queryWrapper);
    Map<String, Object> map = new HashMap<>();
    map.put("total", res.getTotal());
    map.put("data", res.getRecords());
    return map;
  }
  
  @Override
  public Map<String, Object> selectByCoinKind(String id, Integer currentPage, Integer pageSize, String[] coninKinds) {
    IPage<MonitorTransVO> page = new Page<MonitorTransVO>(currentPage, pageSize);
    QueryWrapper<MonitorTransVO> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("b.user_id", id).eq("b.state", 1).orderByDesc("a.id");
    queryWrapper.in("b.coin_kind", coninKinds);
    IPage<MonitorTransVO> res = monitorTransVOMapper.selectByUserId(page, queryWrapper);
    Map<String, Object> map = new HashMap<>();
    map.put("total", res.getTotal());
    map.put("data", res.getRecords());
    return map;
  }
}
