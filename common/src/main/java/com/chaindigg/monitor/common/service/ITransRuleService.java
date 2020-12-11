package com.chaindigg.monitor.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaindigg.monitor.common.entity.TransRule;
import com.chaindigg.monitor.common.exception.DataBaseException;

import java.util.List;
import java.util.Map;

public interface ITransRuleService extends IService<TransRule> {
  
  List<TransRule> selectAllById(Integer userId, Integer currentPage, Integer pageSize);
  
  Boolean add(List<Map<String, Object>> list) throws DataBaseException;
  
  Boolean delete(Integer id);
  
  Boolean update(List<Map<String, Object>> list) throws DataBaseException;
}
