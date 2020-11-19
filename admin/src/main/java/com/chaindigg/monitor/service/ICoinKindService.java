package com.chaindigg.monitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaindigg.monitor.entity.CoinKind;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author chenghao
 * @since 2020-11-17
 */
public interface ICoinKindService extends IService<CoinKind> {

  List<CoinKind> selectAll(@Nullable String mainChain, @Nullable String coinName, int currentPage, int pageSize);
}