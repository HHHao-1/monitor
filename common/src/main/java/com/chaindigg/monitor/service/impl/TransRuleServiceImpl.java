package com.chaindigg.monitor.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaindigg.monitor.dao.TransRuleMapper;
import com.chaindigg.monitor.entity.TransRule;
import com.chaindigg.monitor.service.ITransRuleService;
import lombok.RequiredArgsConstructor;
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
public class TransRuleServiceImpl extends ServiceImpl<TransRuleMapper, TransRule> implements ITransRuleService {
  //  private final TransRuleMapper transRuleMapper;

  @Override
  public List<TransRule> selectByUserId(String id, int currentPage, int pageSize) {
    IPage<TransRule> page = new Page<TransRule>(currentPage, pageSize);
    return this.baseMapper.selectByUserId(page, id);
    //    return transRuleMapper.selectByUserId(id);
  }

  public List<TransRule> selectAll(int currentPage, int pageSize) {
    IPage<TransRule> page = new Page<TransRule>(currentPage, pageSize);
    return this.baseMapper.selectAll(page);
  }
}
