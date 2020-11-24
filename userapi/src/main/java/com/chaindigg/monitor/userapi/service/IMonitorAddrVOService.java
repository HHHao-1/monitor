package com.chaindigg.monitor.userapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaindigg.monitor.userapi.vo.MonitorAddrVO;

import java.util.List;

/**
 * 服务类
 *
 * @author chenghao
 * @since 2020-11-17
 */
public interface IMonitorAddrVOService extends IService<MonitorAddrVO> {
  List<MonitorAddrVO> selectByUserId(String id, int currentPage, int pageSize);

}
