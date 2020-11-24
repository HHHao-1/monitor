package com.chaindigg.monitor.common.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chaindigg.monitor.common.dao.AddrRuleMapper;
import com.chaindigg.monitor.common.dao.UserMapper;
import com.chaindigg.monitor.common.entity.AddrRule;
import com.chaindigg.monitor.common.entity.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SearchField {
  private final UserMapper userMapper;

  private final AddrRuleMapper addrRuleMapper;

  public Integer userId(String name) {
    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
    queryWrapper.select("id");
    return userMapper.selectOne(queryWrapper).getId();
  }

  public Integer userState(String id) {
    QueryWrapper<AddrRule> queryWrapper = new QueryWrapper<>();
    queryWrapper.select("state");
    return addrRuleMapper.selectOne(queryWrapper).getState();
  }

  public Integer addrRuleState(String state) {
    QueryWrapper<AddrRule> queryWrapper = new QueryWrapper<>();
    queryWrapper.select("state");
    return addrRuleMapper.selectOne(queryWrapper).getState();
  }
}
