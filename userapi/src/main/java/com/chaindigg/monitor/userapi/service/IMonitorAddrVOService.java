package com.chaindigg.monitor.userapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaindigg.monitor.userapi.vo.MonitorAddrVO;

import java.util.Map;

/**
 * 服务类
 *
 * @author chenghao
 * @since 2020-11-17
 */
public interface IMonitorAddrVOService extends IService<MonitorAddrVO> {
  Map<String, Object> selectByUserId(String id, Integer currentPage, Integer pageSize);
  
  Map<String, Object> selectByCoinKind(String id, Integer currentPage, Integer pageSize, String[] coninKinds);
  
  Map<String, Object> selectByEvent(String id, Integer currentPage, Integer pageSize, String eventName);
  
  Map<String, Object> selectByMark(String id, Integer currentPage, Integer pageSize, String mark);
  
}
