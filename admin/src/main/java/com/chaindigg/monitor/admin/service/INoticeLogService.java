package com.chaindigg.monitor.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaindigg.monitor.admin.vo.NoticeLogVO;

import java.util.Map;

/**
 * 服务类
 *
 * @author chenghao
 * @since 2020-11-17
 */
public interface INoticeLogService extends IService<NoticeLogVO> {
  
  Map<String, Object> selectAddrAll(String userName, String eventName, String coinKind, Integer currentPage,
                                    Integer pageSize);
  
  Map<String, Object> selectTransAll(String userName, String coinKind, Integer currentPage, Integer pageSize);
  
  Map<String, Object> selectAll(String userName,
                                String monitorType, String eventName, String coinKind, Integer currentPage, Integer pageSize);
}
