package com.chaindigg.monitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaindigg.monitor.vo.AddrRuleVO;

import java.util.List;

/**
 * 服务类
 *
 * @author chenghao
 * @since 2020-11-17
 */
public interface IAddrRuleVOService extends IService<AddrRuleVO> {

  List<AddrRuleVO> selectAll(
      String event, String userName, String userId, int currentPage, int pageSize);
}
