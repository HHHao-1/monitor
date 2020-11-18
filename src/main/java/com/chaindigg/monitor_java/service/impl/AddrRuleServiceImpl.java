package com.chaindigg.monitor_java.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaindigg.monitor_java.dao.AddrRuleMapper;
import com.chaindigg.monitor_java.po.AddrRule;
import com.chaindigg.monitor_java.service.IAddrRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chenghao
 * @since 2020-11-17
 */
@Service
@RequiredArgsConstructor
public class AddrRuleServiceImpl extends ServiceImpl<AddrRuleMapper, AddrRule> implements IAddrRuleService {
  private final AddrRuleMapper addrRuleMapper;

  @Override
  public List<AddrRule> selectByUserId(String id) {
    return addrRuleMapper.selectByUserId(id);
  }

  public List<AddrRule> selectAll() {
    return addrRuleMapper.selectAll();
  }
}
