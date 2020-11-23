package com.chaindigg.monitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaindigg.monitor.vo.MonitorTransVO;

import java.util.List;

/**
 * 服务类
 *
 * @author chenghao
 * @since 2020-11-17
 */
public interface IMonitorTransVOService extends IService<MonitorTransVO> {
  List<MonitorTransVO> selectByUserId(String id, int currentPage, int pageSize);
  //  List<MonitorTransVO> selectAll(int currentPage, int pageSize);
}
