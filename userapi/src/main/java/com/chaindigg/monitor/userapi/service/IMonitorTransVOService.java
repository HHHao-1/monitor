package com.chaindigg.monitor.userapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaindigg.monitor.userapi.vo.MonitorTransVO;

import java.util.List;

/**
 * 服务类
 *
 * @author chenghao
 * @since 2020-11-17
 */
public interface IMonitorTransVOService extends IService<MonitorTransVO> {
  List<MonitorTransVO> selectByUserId(String id, Integer currentPage, Integer pageSize);
}
