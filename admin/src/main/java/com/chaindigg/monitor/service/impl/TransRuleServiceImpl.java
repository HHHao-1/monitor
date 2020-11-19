package com.chaindigg.monitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaindigg.monitor.dao.TransRuleMapper;
import com.chaindigg.monitor.entity.TransRule;
import com.chaindigg.monitor.service.ITransRuleService;
import com.chaindigg.monitor.vo.AddrRuleVO;
import com.chaindigg.monitor.vo.TransRuleVO;
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
public class TransRuleServiceImpl extends ServiceImpl<TransRuleMapper, TransRuleVO> implements ITransRuleService {

  public List<TransRuleVO> selectAll(@Nullable String coin, @Nullable String userName, @Nullable String userId, int currentPage, int pageSize) {
    IPage<TransRuleVO> page = new Page<TransRuleVO>(currentPage, pageSize);
    QueryWrapper<TransRuleVO> queryWrapper = new QueryWrapper<>();
    queryWrapper.orderByDesc("id");
    if(!StringUtils.isBlank(userId)){
      queryWrapper.eq("user_id",userId);
    }else if(!StringUtils.isBlank(userName)&& !StringUtils.isBlank(coin)){
      queryWrapper.eq("name",userName).eq("coin_kind",coin);
    }else if (!StringUtils.isBlank(userName) && StringUtils.isBlank(coin)){
      queryWrapper.eq("name",userName);
    }else if(StringUtils.isBlank(userName) && !StringUtils.isBlank(coin)){
      queryWrapper.eq("coin_kind",coin);
    }
    return this.baseMapper.selectAll(queryWrapper, page).getRecords();
  }
}
