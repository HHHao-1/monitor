package com.chaindigg.monitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaindigg.monitor.entity.AddrRule;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author chenghao
 * @since 2020-11-17
 */
public interface IAddrRuleService extends IService<AddrRule> {
  List<AddrRule> selectByUserId(String id, int currentPage, int pageSize);

  List<AddrRule> selectAll(int currentPage, int pageSize);
}
