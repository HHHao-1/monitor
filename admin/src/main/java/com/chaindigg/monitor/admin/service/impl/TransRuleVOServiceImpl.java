package com.chaindigg.monitor.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaindigg.monitor.admin.dao.TransRuleVOMapper;
import com.chaindigg.monitor.admin.service.ITransRuleVOService;
import com.chaindigg.monitor.admin.vo.TransRuleVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务实现类
 *
 * @author chenghao
 * @since 2020-11-17
 */
@Service
@RequiredArgsConstructor
public class TransRuleVOServiceImpl extends ServiceImpl<TransRuleVOMapper, TransRuleVO>
    implements ITransRuleVOService {
  
  public Map<String, Object> selectAll(
      List<String> coin, String userName, String userId, Integer currentPage, Integer pageSize) {
    IPage<TransRuleVO> page = new Page<TransRuleVO>(currentPage, pageSize);
    QueryWrapper<TransRuleVO> queryWrapper = new QueryWrapper<>();
    queryWrapper.orderByDesc("a.id");
    if (!StringUtils.isBlank(userId)) {
      queryWrapper.eq("a.user_id", userId);
    }
    if (!StringUtils.isBlank(userName)) {
      queryWrapper.eq("b.name", userName);
    }
    if (coin.size() != 0) {
      queryWrapper.in("a.coin_kind", coin);
    }
    IPage<TransRuleVO> res = this.baseMapper.selectAll(queryWrapper, page);
    Map<String, Object> map = new HashMap<>();
    map.put("total", res.getTotal());
    map.put("data", res.getRecords());
    return map;
  }
}
