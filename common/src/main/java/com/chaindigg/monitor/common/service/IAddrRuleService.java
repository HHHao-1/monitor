package com.chaindigg.monitor.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaindigg.monitor.common.entity.AddrRule;
import com.chaindigg.monitor.common.exception.DataBaseException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface IAddrRuleService extends IService<AddrRule> {

  Integer searchUserId(String name);

  List<AddrRule> selectAllById(Integer id, int currentPage, int pageSize);

  Boolean add(List<Map<String, Object>> list) throws DataBaseException;

  Boolean delete(String userName, String eventName, LocalDateTime AddTime);

  Boolean update(List<Map<String, Object>> list) throws DataBaseException;
}
