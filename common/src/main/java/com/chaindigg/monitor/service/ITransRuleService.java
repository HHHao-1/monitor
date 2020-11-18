package com.chaindigg.monitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaindigg.monitor.entity.TransRule;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author chenghao
 * @since 2020-11-17
 */
public interface ITransRuleService extends IService<TransRule> {
  List<TransRule> selectByUserId(String id, int currentPage, int pageSize);

  List<TransRule> selectAll(int currentPage, int pageSize);
}
