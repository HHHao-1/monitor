package com.chaindigg.monitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaindigg.monitor.dao.CoinKindMapper;
import com.chaindigg.monitor.entity.CoinKind;
import com.chaindigg.monitor.entity.User;
import com.chaindigg.monitor.service.ICoinKindService;
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
public class CoinKindServiceImpl extends ServiceImpl<CoinKindMapper, CoinKind> implements ICoinKindService {

  @Override
  public List<CoinKind> selectAll(@Nullable String mainChain, @Nullable String coinName, int currentPage, int pageSize) {
    IPage<CoinKind> page = new Page<CoinKind>(currentPage, pageSize);
    QueryWrapper<CoinKind> queryWrapper = new QueryWrapper<>();
    queryWrapper.orderByDesc("id");
    if (!StringUtils.isBlank(mainChain) && !StringUtils.isBlank(coinName)){
      queryWrapper.eq("main_chain",mainChain).eq("coin_name",coinName);
    }else if(!StringUtils.isBlank(mainChain) && StringUtils.isBlank(coinName)){
      queryWrapper.eq("main_chain",mainChain);
    }else if(StringUtils.isBlank(mainChain) && !StringUtils.isBlank(coinName)){
      queryWrapper.eq("coin_name",coinName);
    }
    return this.page(page,queryWrapper).getRecords();
  }
}
