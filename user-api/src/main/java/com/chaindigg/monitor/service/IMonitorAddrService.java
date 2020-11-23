package com.chaindigg.monitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaindigg.monitor.entity.MonitorAddr;
import com.chaindigg.monitor.vo.MonitorAddrVO;

import java.util.List;

/**
 * 服务类
 *
 * @author chenghao
 * @since 2020-11-17
 */
public interface IMonitorAddrService extends IService<MonitorAddr> {
  List<MonitorAddrVO> selectByUserId(String id, int currentPage, int pageSize);

  List<MonitorAddrVO> selectAll(int currentPage, int pageSize);
}
