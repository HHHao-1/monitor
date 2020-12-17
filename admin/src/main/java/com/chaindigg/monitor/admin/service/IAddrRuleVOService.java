package com.chaindigg.monitor.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaindigg.monitor.admin.vo.AddrRuleVO;

import java.util.Map;

/**
 * 服务类
 *
 * @author chenghao
 * @since 2020-11-17
 */
public interface IAddrRuleVOService extends IService<AddrRuleVO> {
  
  Map<String, Object> selectAll(
      String event, String userName, String userId, Integer currentPage, Integer pageSize);
}
