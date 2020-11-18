package com.chaindigg.monitor_java.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaindigg.monitor_java.dao.TransRuleMapper;
import com.chaindigg.monitor_java.po.TransRule;
import com.chaindigg.monitor_java.service.ITransRuleService;
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
public class TransRuleServiceImpl extends ServiceImpl<TransRuleMapper, TransRule> implements ITransRuleService {
  private final TransRuleMapper transRuleMapper;

  @Override
  public List<TransRule> selectByUserId(String id) {
    return transRuleMapper.selectByUserId(id);
  }

  public List<TransRule> selectAll() {
    return this.list();
  }
}
