package com.chaindigg.monitor.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chaindigg.monitor.vo.NoticeLogVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.lang.Nullable;

import java.util.List;

@Mapper
public interface NoticeLogMapper extends BaseMapper<NoticeLogVO> {
  @Select("select a.coin_kind, a.notice_way, b.notice_time, c.name " +
      "from trans_rule a join monitor_trans b " +
      "on a.id = b.trans_rule_id join user c on a.user_id = c.id ")
  List<NoticeLogVO> selectTransAll(@Nullable Wrapper wrapper, IPage... page);

  @Select("select a.event_name, a.coin_kind, a.notice_way, b.notice_time, c.name " +
      "from addr_rule a join monitor_addr b " +
      "on a.id = b.addr_rule_id join user c on a.user_id = c.id ")
  List<NoticeLogVO> selectAddrAll(@Nullable Wrapper wrapper, IPage... page);
}
