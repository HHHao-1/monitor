package com.chaindigg.monitor_java.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaindigg.monitor_java.dto.NoticeLogDTO;

import java.util.List;
import java.util.Map;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chenghao
 * @since 2020-11-17
 */

public interface INoticeLogService extends IService<NoticeLogDTO> {
  List<NoticeLogDTO> selectAddrAll();
  List<NoticeLogDTO> selectTransAll();
  Map<String, List<NoticeLogDTO>> selectAll();
}
