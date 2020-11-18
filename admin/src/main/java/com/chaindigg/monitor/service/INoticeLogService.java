package com.chaindigg.monitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaindigg.monitor.vo.NoticeLogVO;

import java.util.List;


/**
 * <p>
 * 服务类
 * </p>
 *
 * @author chenghao
 * @since 2020-11-17
 */

public interface INoticeLogService extends IService<NoticeLogVO> {
  List<NoticeLogVO> selectAddrAll(int currentPage, int pageSize);

  List<NoticeLogVO> selectTransAll(int currentPage, int pageSize);

  List<NoticeLogVO> selectAll(int currentPage, int pageSize);
}
