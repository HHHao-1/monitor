package com.chaindigg.monitor.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaindigg.monitor.common.entity.CoinKind;
import org.apache.ibatis.annotations.Mapper;

/**
 * (coin_kind)数据Mapper
 *
 * @author chenghao
 * @since 2020-11-17 14:46:20
 */
@Mapper
public interface CoinKindMapper extends BaseMapper<CoinKind> {}
