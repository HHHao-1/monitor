package com.chaindigg.monitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaindigg.monitor.entity.TransRule;
import com.chaindigg.monitor.exception.DataBaseException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ITransRuleService extends IService<TransRule> {

  Boolean add(List<Map<String, Object>> list) throws DataBaseException;

  Boolean delete(String userName, String coinKind, LocalDateTime AddTime);

  Boolean update(List<Map<String, Object>> list) throws DataBaseException;
}
