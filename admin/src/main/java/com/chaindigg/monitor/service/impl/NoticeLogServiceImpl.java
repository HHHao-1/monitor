package com.chaindigg.monitor.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaindigg.monitor.dao.NoticeLogMapper;
import com.chaindigg.monitor.entity.TransRule;
import com.chaindigg.monitor.vo.NoticeLogVO;
import com.chaindigg.monitor.service.INoticeLogService;
import lombok.RequiredArgsConstructor;
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
  public List<NoticeLogVO> selectAddrAll(int currentPage, int pageSize) {
    IPage<TransRule> page = new Page<TransRule>(currentPage, pageSize);
    return this.baseMapper.selectAddrAll(page);
  }

  @Override
  public List<NoticeLogVO> selectTransAll(int currentPage, int pageSize) {
    IPage<TransRule> page = new Page<TransRule>(currentPage, pageSize);
    return this.baseMapper.selectTransAll(page);
  }

  @Override
  public List<NoticeLogVO> selectAll(int currentPage, int pageSize) {
    List<NoticeLogVO> list = this.baseMapper.selectAddrAll();
    if (list.addAll(this.baseMapper.selectTransAll())) {
      list.stream().sorted((p1, p2) -> p2.getNoticeTime().compareTo(p1.getNoticeTime())).collect(Collectors.toList());
      list.subList(currentPage * pageSize - 1, currentPage * pageSize - 1 + pageSize);
      return list;
    }
    return null;
  }
}
