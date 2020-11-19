package com.chaindigg.monitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaindigg.monitor.vo.NoticeLogVO;
import org.springframework.lang.Nullable;

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

  List<NoticeLogVO> selectAddrAll(@Nullable String eventName, @Nullable String coinKind, int currentPage, int pageSize);


  List<NoticeLogVO> selectTransAll(@Nullable String coinKind, int currentPage, int pageSize);


  List<NoticeLogVO> selectAll(@Nullable String monitorType, @Nullable String eventName, @Nullable String coinKind, int currentPage, int pageSize);
}
