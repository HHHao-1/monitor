package com.chaindigg.monitor.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaindigg.monitor.admin.dao.NoticeLogVOMapper;
import com.chaindigg.monitor.admin.service.INoticeLogService;
import com.chaindigg.monitor.admin.vo.NoticeLogVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 服务实现类
 *
 * @author chenghao
 * @since 2020-11-17
 */
@RequiredArgsConstructor
@Service
public class NoticeLogServiceImpl extends ServiceImpl<NoticeLogVOMapper, NoticeLogVO>
    implements INoticeLogService {
  
  @Override
  public Map<String, Object> selectAddrAll(Integer ruleId, String userName,
                                           String eventName, List<String> coinKind, Integer currentPage, Integer pageSize) {
    IPage<NoticeLogVO> page = new Page<NoticeLogVO>(currentPage, pageSize);
    QueryWrapper<NoticeLogVO> queryWrapper = new QueryWrapper<>();
    if (ruleId != null) {
      queryWrapper.eq("b.addr_rule_id", ruleId);
    }
    // 自定义查询最好加上别名.字段，否则字段查询可能会找不到（模糊不清）
    queryWrapper.orderByDesc("b.id");
    if ((!StringUtils.isBlank(eventName))) {
      queryWrapper.eq("a.event_name", eventName);
    }
    if (coinKind != null) {
      queryWrapper.in("a.coin_kind", coinKind);
    }
    if (!StringUtils.isBlank(userName)) {
      queryWrapper.eq("c.name", userName);
    }
    IPage<NoticeLogVO> res = this.baseMapper.selectAddrAll(queryWrapper, page);
    Map<String, Object> map = new HashMap<>();
    map.put("total", res.getTotal());
    map.put("data", res.getRecords());
    return map;
  }
  
  @Override
  public Map<String, Object> selectTransAll(Integer ruleId, String userName, List<String> coinKind, Integer currentPage,
                                            Integer pageSize) {
    IPage<NoticeLogVO> page = new Page<NoticeLogVO>(currentPage, pageSize);
    QueryWrapper<NoticeLogVO> queryWrapper = new QueryWrapper<>();
    if (ruleId != null) {
      queryWrapper.eq("b.trans_rule_id", ruleId);
    }
    queryWrapper.orderByDesc("b.id");
    if (coinKind != null) {
      queryWrapper.in("a.coin_kind", coinKind);
    }
    if (!StringUtils.isBlank(userName)) {
      queryWrapper.eq("c.name", userName);
    }
    IPage<NoticeLogVO> res = this.baseMapper.selectTransAll(queryWrapper, page);
    Map<String, Object> map = new HashMap<>();
    map.put("total", res.getTotal());
    map.put("data", res.getRecords());
    return map;
  }
  
  @Override
  public Map<String, Object> selectAll(String userName,
                                       String monitorType, String eventName, List<String> coinKind, Integer currentPage,
                                       Integer pageSize) {
    Map<String, Object> map = new HashMap<>();
    IPage<NoticeLogVO> page = new Page<NoticeLogVO>(currentPage, pageSize);
    QueryWrapper<NoticeLogVO> queryWrapper = new QueryWrapper<>();
    queryWrapper.orderByDesc("b.id");
    if (!StringUtils.isBlank(monitorType)) {
      switch (monitorType) {
        case "addr":
          if (!StringUtils.isBlank(eventName)) {
            if (coinKind != null) {
              if (!StringUtils.isBlank(userName)) {
                return selectAddrAll(null, userName, eventName, coinKind, currentPage, pageSize);
              }
              return selectAddrAll(null, null, eventName, coinKind, currentPage, pageSize);
            }
            return selectAddrAll(null, null, eventName, null, currentPage, pageSize);
          } else if (coinKind != null) {
            if (!StringUtils.isBlank(userName)) {
              return selectAddrAll(null, userName, null, coinKind, currentPage, pageSize);
            }
            return selectAddrAll(null, null, eventName, coinKind, currentPage, pageSize);
          } else if (!StringUtils.isBlank(userName)) {
            return selectAddrAll(null, userName, null, null, currentPage, pageSize);
          } else {
            return selectAddrAll(null, null, null, null, currentPage, pageSize);
          }
//          if (!StringUtils.isBlank(eventName) && StringUtils.isBlank(coinKind)) {
//            return selectAddrAll(eventName, null, currentPage, pageSize);
//          } else if (!StringUtils.isBlank(coinKind) && StringUtils.isBlank(eventName)) {
//            return selectAddrAll(null, coinKind, currentPage, pageSize);
//          } else if (!StringUtils.isBlank(eventName) && !StringUtils.isBlank(coinKind)) {
//            return selectAddrAll(eventName, coinKind, currentPage, pageSize);
//          } else {
//            return selectAddrAll(null, null, currentPage, pageSize);
//          }
        case "trans":
          if (coinKind != null) {
            if (!StringUtils.isBlank(userName)) {
              return selectTransAll(null, userName, coinKind, currentPage, pageSize);
            }
            return selectTransAll(null, null, coinKind, currentPage, pageSize);
          } else if (!StringUtils.isBlank(userName)) {
            return selectTransAll(null, userName, null, currentPage, pageSize);
          } else {
            return selectTransAll(null, null, null, currentPage, pageSize);
          }
        default:
          break;
      }
    }
    
    // 这里要查全部，pageSize取long的最大值
//    page = new Page<NoticeLogVO>(0L, 9223372036854775807L);
    IPage<NoticeLogVO> page1 = new Page<NoticeLogVO>();
    List<NoticeLogVO> listl = this.baseMapper.selectTransAll(queryWrapper, page1).getRecords();
    List<NoticeLogVO> list = this.baseMapper.selectAddrAll(queryWrapper, page1).getRecords();
    list.addAll(this.baseMapper.selectTransAll(queryWrapper, page1).getRecords());
    list =
        list.stream()
            .sorted((p1, p2) -> p2.getUnusualTime().compareTo(p1.getUnusualTime()))
            .collect(Collectors.toList());
    if (userName != null) {
      list = list.stream().filter(s -> Objects.equals(s.getUserName(), userName)).collect(Collectors.toList());
    }
    map.put("total", list.size());
    int start = 0;
    int end = 0;
    if ((currentPage * pageSize) > list.size()) {
      start = (currentPage - 1) * pageSize;
      end = list.size();
    } else {
      start = (currentPage - 1) * pageSize;
      end = currentPage * pageSize;
    }
    list = list.subList(start, end);
//    if (!StringUtils.isBlank(eventName) && !StringUtils.isBlank(coinKind)) {
    if (!StringUtils.isBlank(eventName) && coinKind != null) {
      List<NoticeLogVO> listquery =
          list.stream()
              .filter(e -> Objects.equals(e.getEventName(), eventName))
//              .filter(e -> Objects.equals(e.getCoinKind(), coinKind))
              .filter(e -> coinKind.contains(e.getCoinKind()))
              .collect(Collectors.toList());
      map.put("data", listquery);
      return map;
    } else if (!StringUtils.isBlank(eventName) && coinKind == null) {
      List<NoticeLogVO> listquery =
          list.stream()
              .filter(e -> Objects.equals(e.getEventName(), eventName))
              .collect(Collectors.toList());
      map.put("data", listquery);
      return map;
    } else if (StringUtils.isBlank(eventName) && coinKind != null) {
      List<NoticeLogVO> listquery =
          list.stream()
              .filter(e -> coinKind.contains(e.getCoinKind()))
              .collect(Collectors.toList());
      map.put("data", listquery);
      return map;
    }
    map.put("data", list);
    return map;
  }
}
