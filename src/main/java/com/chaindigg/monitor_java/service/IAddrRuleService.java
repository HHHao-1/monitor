package com.chaindigg.monitor_java.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaindigg.monitor_java.po.AddrRule;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chenghao
 * @since 2020-11-17
 */
public interface IAddrRuleService extends IService<AddrRule> {
  List<AddrRule> selectByUserId(String id);
  List<AddrRule> selectAll();
}
