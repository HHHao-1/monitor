package com.chaindigg.monitor.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaindigg.monitor.admin.dao.CoinKindMapper;
import com.chaindigg.monitor.admin.service.ICoinKindService;
import com.chaindigg.monitor.common.entity.CoinKind;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 服务实现类
 *
 * @author chenghao
 * @since 2020-11-17
 */
@Service
public class CoinKindServiceImpl extends ServiceImpl<CoinKindMapper, CoinKind>
    implements ICoinKindService {

  @Override
  public List<CoinKind> selectAll(
      String mainChain, String coinName, int currentPage, int pageSize) {
    IPage<CoinKind> page = new Page<CoinKind>(currentPage, pageSize);
    QueryWrapper<CoinKind> queryWrapper = new QueryWrapper<>();
    queryWrapper.orderByDesc("id");
    if (!StringUtils.isBlank(mainChain)) {
      queryWrapper.eq("main_chain", mainChain);
    }
    if (!StringUtils.isBlank(coinName)) {
      queryWrapper.eq("coin_name", coinName);
    }
    return this.page(page, queryWrapper).getRecords();
  }

  @Override
  public Boolean add(String mainChain, String coinName, String contract, Integer point) {
    CoinKind coinKind = new CoinKind();
    coinKind
        .setMainChain(mainChain)
        .setCoinName(coinName)
        .setContractAddr(contract)
        .setPoint(point)
        .setCreateTime(LocalDateTime.now())
        .setUpdateTime(LocalDateTime.now());
    return this.save(coinKind);
  }

  @Override
  public Boolean delete(String mainChain, String coinName, String contract, Integer point) {
    QueryWrapper<CoinKind> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("main_chain", mainChain).eq("point", point);
    if (!StringUtils.isBlank(coinName)) {
      queryWrapper.eq("coin_name", coinName);
    }
    if (!StringUtils.isBlank(contract)) {
      queryWrapper.eq("contract_addr", contract);
    }
    return this.remove(queryWrapper);
  }

  @Override
  public Boolean update(
      String mainChain,
      String coinName,
      String contract,
      Integer point,
      String mainChainNew,
      String coinNameNew,
      String contractNew,
      Integer pointNew) {
    UpdateWrapper<CoinKind> updateWrapper = new UpdateWrapper<>();
    updateWrapper.eq("main_chain", mainChain).eq("point", point);
    if (!StringUtils.isBlank(coinName)) {
      updateWrapper.eq("coin_name", coinName);
    }
    if (!StringUtils.isBlank(contract)) {
      updateWrapper.eq("contract_addr", contract);
    }
    if (!StringUtils.isBlank(mainChainNew)) {
      updateWrapper.set("main_chain", mainChainNew);
      updateWrapper.set("update_time", LocalDateTime.now());
    }
    if (!StringUtils.isBlank(coinNameNew)) {
      updateWrapper.set("coin_name", coinNameNew);
      updateWrapper.set("update_time", LocalDateTime.now());
    }
    if (!StringUtils.isBlank(contractNew)) {
      updateWrapper.set("contract_addr", contractNew);
      updateWrapper.set("update_time", LocalDateTime.now());
    }
    if (pointNew != null) {
      updateWrapper.set("point", pointNew);
      updateWrapper.set("update_time", LocalDateTime.now());
    }
    return this.update(updateWrapper);
  }
}
