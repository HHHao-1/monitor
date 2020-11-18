package com.chaindigg.monitor_java.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaindigg.monitor_java.dto.MonitorAddrDTO;
import com.chaindigg.monitor_java.po.MonitorAddr;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chenghao
 * @since 2020-11-17
 */
public interface IMonitorAddrService extends IService<MonitorAddr> {
  List<MonitorAddrDTO> selectByUserId (String id);

  List<MonitorAddrDTO> selectAll ();
}
