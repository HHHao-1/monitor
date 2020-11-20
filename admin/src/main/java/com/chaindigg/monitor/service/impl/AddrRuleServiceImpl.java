package com.chaindigg.monitor.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaindigg.monitor.dao.AddrRuleMapper;
import com.chaindigg.monitor.service.IAddrRuleService;
import com.chaindigg.monitor.vo.AddrRuleVO;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author chenghao
 * @since 2020-11-17
 */
@Service
@RequiredArgsConstructor
public class AddrRuleServiceImpl extends ServiceImpl<AddrRuleMapper, AddrRuleVO> implements IAddrRuleService {

  public List<AddrRuleVO> selectAll(@Nullable String event, @Nullable String userName, @Nullable String userId, int currentPage, int pageSize) {
    IPage<AddrRuleVO> page = new Page<AddrRuleVO>(currentPage, pageSize);
    QueryWrapper<AddrRuleVO> queryWrapper = new QueryWrapper<>();
    queryWrapper.orderByDesc("id");
    if (!StringUtils.isBlank(userId)){
      queryWrapper.eq("user_id",userId);
    }
    if (!StringUtils.isBlank(userName)){
      queryWrapper.eq("name",userName);
    }
    if (!StringUtils.isBlank(event)){
      queryWrapper.eq("event_name", event);
    }
    return this.baseMapper.selectAll(queryWrapper, page).getRecords();
  }
}
