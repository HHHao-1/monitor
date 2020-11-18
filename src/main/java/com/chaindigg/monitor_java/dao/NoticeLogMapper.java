package com.chaindigg.monitor_java.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaindigg.monitor_java.dto.NoticeLogDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NoticeLogMapper extends BaseMapper<NoticeLogDTO> {
  @Select("select a.coin_kind, a.notice_way, b.notice_time, c.name " +
      "from trans_rule a join monitor_trans b " +
      "on a.id = b.trans_rule_id join user c on a.user_id = c.id")
  List<NoticeLogDTO> selectTransAll();

  @Select("select a.event_name, a.coin_kind, a.notice_way, b.notice_time, c.name " +
      "from addr_rule a join monitor_addr b " +
      "on a.id = b.addr_rule_id join user c on a.user_id = c.id")
  List<NoticeLogDTO> selectAddrAll();
}
