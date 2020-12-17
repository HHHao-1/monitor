package com.chaindigg.monitor.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaindigg.monitor.common.entity.AddrRule;
import com.chaindigg.monitor.common.exception.DataBaseException;

import java.util.List;
import java.util.Map;

public interface IAddrRuleService extends IService<AddrRule> {
  
  Integer searchUserId(String name);
  
  Map<String, Object> selectAllById(Integer userId, Integer currentPage, Integer pageSize);
  
  AddrRule selectAllByUId(Integer id);
  
  Boolean add(List<Map<String, Object>> list) throws DataBaseException;
  
  Boolean delete(Integer id);
  
  Boolean update(List<Map<String, Object>> list) throws DataBaseException;
}
