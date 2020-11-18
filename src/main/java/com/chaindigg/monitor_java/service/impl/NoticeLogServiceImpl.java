package com.chaindigg.monitor_java.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaindigg.monitor_java.dao.NoticeLogMapper;
import com.chaindigg.monitor_java.dto.NoticeLogDTO;
import com.chaindigg.monitor_java.service.INoticeLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chenghao
 * @since 2020-11-17
 */

@RequiredArgsConstructor
@Service
public class NoticeLogServiceImpl extends ServiceImpl<NoticeLogMapper , NoticeLogDTO>  implements INoticeLogService {

  private final NoticeLogMapper noticeLogMapper;
  @Override
  public List<NoticeLogDTO> selectAddrAll() {
    return noticeLogMapper.selectAddrAll();
  }

  @Override
  public List<NoticeLogDTO> selectTransAll() {
    return noticeLogMapper.selectTransAll();
  }

  @Override
  public Map<String, List<NoticeLogDTO>> selectAll() {
    Map<String, List<NoticeLogDTO>> map = new HashMap<>();
    map.put("地址异动监控",noticeLogMapper.selectAddrAll());
    map.put("大额交易监控",noticeLogMapper.selectTransAll());
    return map;
  }
}
