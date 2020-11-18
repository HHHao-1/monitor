package com.chaindigg.monitor_java.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaindigg.monitor_java.dto.MonitorAddrDTO;
import com.chaindigg.monitor_java.po.MonitorAddr;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * (monitor_addr)数据Mapper
 *
 * @author chenghao
 * @since 2020-11-17 15:33:37
 *
*/
@Mapper
public interface MonitorAddrMapper extends BaseMapper<MonitorAddr> {

  @Select("select a.trans_hash, a.unusual_count, a.unusual_time, b.event_name, b.coin_kind, b.address, b.address_mark " +
      "from monitor_addr a join addr_rule b " +
      "on a.addr_rule_id = b.id " +
      "where b.user_id = #{id}")
  List<MonitorAddrDTO> selectByUserId(String id);

  @Select("select a.trans_hash, a.unusual_count, a.unusual_time, b.event_name, b.coin_kind, b.address, b.address_mark " +
      "from monitor_addr a join addr_rule b " +
      "on a.addr_rule_id = b.id ")
  List<MonitorAddrDTO> selectAll();
}
