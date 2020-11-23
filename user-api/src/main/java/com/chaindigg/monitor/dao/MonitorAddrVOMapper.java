package com.chaindigg.monitor.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.chaindigg.monitor.vo.MonitorAddrVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * (monitor_addr)数据Mapper
 *
 * @author chenghao
 * @since 2020-11-17 15:33:37
 */
@Mapper
public interface MonitorAddrVOMapper extends BaseMapper<MonitorAddrVO> {

  @Select(
      "select a.trans_hash, a.unusual_count, a.unusual_time, b.event_name, b.coin_kind, b.address, b.address_mark "
          + "from monitor_addr a join addr_rule b "
          + "on a.addr_rule_id = b.id ${ew.customSqlSegment}")
  IPage<MonitorAddrVO> selectByUserId(IPage page, @Param(Constants.WRAPPER) Wrapper wrapper);

  @Select(
      "select a.trans_hash, a.unusual_count, a.unusual_time, b.event_name, b.coin_kind, b.address, b.address_mark "
          + "from monitor_addr a join addr_rule b "
          + "on a.addr_rule_id = b.id ${ew.customSqlSegment}")
  IPage<MonitorAddrVO> selectAll(IPage page, @Param(Constants.WRAPPER) Wrapper wrapper);
}
