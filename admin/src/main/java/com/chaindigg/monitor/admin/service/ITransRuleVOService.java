package com.chaindigg.monitor.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaindigg.monitor.admin.vo.TransRuleVO;

import java.util.List;
import java.util.Map;

/**
 * 服务类
 *
 * @author chenghao
 * @since 2020-11-17
 */
public interface ITransRuleVOService extends IService<TransRuleVO> {
  
  Map<String, Object> selectAll(
      List<String> coin, String userName, String userId, Integer currentPage, Integer pageSize);
}
