package com.chaindigg.monitor.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaindigg.monitor.admin.vo.NoticeLogVO;

import java.util.List;
import java.util.Map;

/**
 * 服务类
 *
 * @author chenghao
 * @since 2020-11-17
 */
public interface INoticeLogService extends IService<NoticeLogVO> {
  
  Map<String, Object> selectAddrAll(Integer ruleId, String userName, String eventName, List<String> coinKind,
                                    Integer currentPage,
                                    Integer pageSize);
  
  Map<String, Object> selectTransAll(Integer ruleId, String userName, List<String> coinKind, Integer currentPage,
                                     Integer pageSize);
  
  Map<String, Object> selectAll(String userName,
                                String monitorType, String eventName, List<String> coinKind, Integer currentPage,
                                Integer pageSize);
}
