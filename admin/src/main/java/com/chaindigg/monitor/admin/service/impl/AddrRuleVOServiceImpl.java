package com.chaindigg.monitor.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaindigg.monitor.admin.dao.AddrRuleVOMapper;
import com.chaindigg.monitor.admin.service.IAddrRuleVOService;
import com.chaindigg.monitor.admin.vo.AddrRuleVO;
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
public class AddrRuleVOServiceImpl extends ServiceImpl<AddrRuleVOMapper, AddrRuleVO>
    implements IAddrRuleVOService {
  
  public List<AddrRuleVO> selectAll(
      String event, String userName, String userId, Integer currentPage, Integer pageSize) {
    IPage<AddrRuleVO> page = new Page<AddrRuleVO>(currentPage, pageSize);
    QueryWrapper<AddrRuleVO> queryWrapper = new QueryWrapper<>();
    queryWrapper.orderByDesc("a.id");
    if (!StringUtils.isBlank(userId)) {
      queryWrapper.eq("a.user_id", userId);
    }
    if (!StringUtils.isBlank(userName)) {
      queryWrapper.eq("b.name", userName);
    }
    if (!StringUtils.isBlank(event)) {
      queryWrapper.eq("a.event_name", event);
    }
    return this.baseMapper.selectAll(queryWrapper, page).getRecords();
  }
}
