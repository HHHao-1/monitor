package com.chaindigg.monitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaindigg.monitor.vo.NoticeLogVO;

import java.util.List;

/**
 * 服务类
 *
 * @author chenghao
 * @since 2020-11-17
 */
public interface INoticeLogService extends IService<NoticeLogVO> {

  List<NoticeLogVO> selectAddrAll(String eventName, String coinKind, int currentPage, int pageSize);

  List<NoticeLogVO> selectTransAll(String coinKind, int currentPage, int pageSize);

  List<NoticeLogVO> selectAll(
      String monitorType, String eventName, String coinKind, int currentPage, int pageSize);
}
