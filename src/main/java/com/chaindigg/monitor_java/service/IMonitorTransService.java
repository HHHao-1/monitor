package com.chaindigg.monitor_java.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaindigg.monitor_java.dto.MonitorTransDTO;
import com.chaindigg.monitor_java.po.MonitorTrans;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chenghao
 * @since 2020-11-17
 */
public interface IMonitorTransService extends IService<MonitorTrans> {
  List<MonitorTransDTO> selectByUserId (String id);

  List<MonitorTransDTO> selectAll ();
}
