package com.chaindigg.monitor.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.chaindigg.monitor.vo.MonitorTransVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * (monitor_trans)数据Mapper
 *
 * @author chenghao
 * @since 2020-11-16 17:40:50
 */
@Mapper
public interface MonitorTransVOMapper extends BaseMapper<MonitorTransVO> {

  @Select(
      "select a.trans_hash, a.from_address, a.to_address, a.unusual_count, a.unusual_time, b.coin_kind "
          + "from monitor_trans a join trans_rule b "
          + "on a.trans_rule_id = b.id ${ew.customSqlSegment}")
  IPage<MonitorTransVO> selectByUserId(IPage page, @Param(Constants.WRAPPER) Wrapper wrapper);

  @Select(
      "select a.trans_hash, a.from_address, a.to_address, a.unusual_count, a.unusual_time, b.coin_kind "
          + "from monitor_trans a join trans_rule b ${ew.customSqlSegment}")
  IPage<MonitorTransVO> selectAll(IPage page, @Param(Constants.WRAPPER) Wrapper wrapper);
}
