package com.chaindigg.monitor.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chaindigg.monitor.vo.NoticeLogVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface NoticeLogVOMapper extends BaseMapper<NoticeLogVO> {

  @Select(
      "select a.coin_kind, a.notice_way, b.notice_time, b.trans_hash, b.unusual_count, b.unusual_time, c.name user_name "
          + "from trans_rule a join monitor_trans b "
          + "on a.id = b.trans_rule_id join user c on a.user_id = c.id ")
  IPage<NoticeLogVO> selectTransAll(Wrapper wrapper, IPage page);

  @Select(
      "select a.event_name, a.coin_kind, a.notice_way, b.notice_time, b.trans_hash, b.unusual_count, b.unusual_time, c.name user_name "
          + "from addr_rule a join monitor_addr b "
          + "on a.id = b.addr_rule_id join user c on a.user_id = c.id ")
  IPage<NoticeLogVO> selectAddrAll(Wrapper wrapper, IPage page);
}
