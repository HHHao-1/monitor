package com.chaindigg.monitor.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaindigg.monitor.dao.NoticeLogMapper;
import com.chaindigg.monitor.entity.TransRule;
import com.chaindigg.monitor.entity.User;
import com.chaindigg.monitor.vo.NoticeLogVO;
import com.chaindigg.monitor.service.INoticeLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author chenghao
 * @since 2020-11-17
 */

@RequiredArgsConstructor
@Service
public class NoticeLogServiceImpl extends ServiceImpl<NoticeLogMapper, NoticeLogVO> implements INoticeLogService {

  @Override
  public List<NoticeLogVO> selectAddrAll(@Nullable String eventName, @Nullable String coinKind, int currentPage, int pageSize) {
    IPage<NoticeLogVO> page = new Page<NoticeLogVO>(currentPage, pageSize);
    QueryWrapper<NoticeLogVO> queryWrapper = new QueryWrapper<>();
    queryWrapper.orderByDesc("id");

    if (!StringUtils.isBlank(eventName) && !StringUtils.isBlank(coinKind)) {
      queryWrapper.and(i -> i.eq("event_name", eventName).ne("coin_kind", coinKind));
    } else if ((!StringUtils.isBlank(eventName)) && StringUtils.isBlank(coinKind)) {
      queryWrapper.eq("event_name", eventName);
    } else if (StringUtils.isBlank(eventName) && !StringUtils.isBlank(coinKind)) {
      queryWrapper.eq("coin_kind", coinKind);
    }

    return this.baseMapper.selectAddrAll(queryWrapper, page);
  }

  @Override
  public List<NoticeLogVO> selectTransAll(@Nullable String coinKind, int currentPage, int pageSize) {
    IPage<NoticeLogVO> page = new Page<NoticeLogVO>(currentPage, pageSize);
    QueryWrapper<NoticeLogVO> queryWrapper = new QueryWrapper<>();
    queryWrapper.orderByDesc("id");
    if (!StringUtils.isBlank(coinKind)) {
      queryWrapper.eq("coin_kind", coinKind);
    }
    return this.baseMapper.selectTransAll(queryWrapper, page);
  }

  @Override
  public List<NoticeLogVO> selectAll(@Nullable String monitorType, @Nullable String eventName, String coinKind, int currentPage, int pageSize) {
    IPage<NoticeLogVO> page = new Page<NoticeLogVO>(currentPage, pageSize);
    QueryWrapper<NoticeLogVO> queryWrapper = new QueryWrapper<>();
    queryWrapper.orderByDesc("id");
    if (!StringUtils.isBlank(monitorType)) {
      switch (monitorType) {
        case "addr":
          if (!StringUtils.isBlank(eventName) && StringUtils.isBlank(coinKind)) {
            selectAddrAll(eventName, null, currentPage, pageSize);
          } else if (!StringUtils.isBlank(coinKind) && StringUtils.isBlank(eventName)) {
            selectAddrAll(null, coinKind, currentPage, pageSize);
          } else if (!StringUtils.isBlank(eventName) && !StringUtils.isBlank(coinKind)) {
            selectAddrAll(eventName, coinKind, currentPage, pageSize);
          } else {
            selectAddrAll(null, null, currentPage, pageSize);
          }
          break;
        case "trans":
          if (!StringUtils.isBlank(coinKind)) {
            selectTransAll(coinKind, currentPage, pageSize);
          } else {
            selectTransAll(null, currentPage, pageSize);
          }
          break;
      }
    } else {
      List<NoticeLogVO> list = this.baseMapper.selectAddrAll(null);
      list.addAll(this.baseMapper.selectTransAll(null));
      list = list.stream()
          .sorted((p1, p2) -> p2.getNoticeTime().compareTo(p1.getNoticeTime()))
          .collect(Collectors.toList());
      int start = 0;
      int end = 0;
      if((currentPage * pageSize) > list.size()){
        end = list.size();
      }else {
        start = (currentPage - 1) * pageSize;
        end = currentPage * pageSize;
      }
      list = list.subList(start, end);
      if (!StringUtils.isBlank(eventName) && !StringUtils.isBlank(coinKind)) {
        List<NoticeLogVO> listquery = list.stream()
            .filter(e -> e.getEventName() == eventName)
            .filter(e -> e.getCoinKind() == coinKind)
            .collect(Collectors.toList());
        return listquery;
      } else if (!StringUtils.isBlank(eventName) && StringUtils.isBlank(coinKind)) {
        List<NoticeLogVO> listquery = list.stream()
            .filter(e -> e.getEventName() == eventName)
            .collect(Collectors.toList());
        return listquery;
      } else if (StringUtils.isBlank(eventName) && !StringUtils.isBlank(coinKind)) {
        List<NoticeLogVO> listquery = list.stream()
            .filter(e -> e.getCoinKind() == coinKind)
            .collect(Collectors.toList());
        return listquery;
      }
      return list;
    }
    return null;
  }
}
