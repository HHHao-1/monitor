package com.chaindigg.monitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaindigg.monitor.entity.AddrRule;
import com.chaindigg.monitor.exception.DataBaseException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface IAddrRuleService extends IService<AddrRule> {

  Boolean add(List<Map<String, Object>> list) throws DataBaseException;

  Boolean delete(String userName, String eventName, LocalDateTime AddTime);

  Boolean update(List<Map<String, Object>> list) throws DataBaseException;
}
