package com.chaindigg.monitor_java.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaindigg.monitor_java.dao.CoinKindMapper;
import com.chaindigg.monitor_java.po.CoinKind;
import com.chaindigg.monitor_java.service.ICoinKindService;
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
public class CoinKindServiceImpl extends ServiceImpl<CoinKindMapper, CoinKind> implements ICoinKindService {

  @Override
  public List<CoinKind> selectAll() {
    return this.list();
  }
}
