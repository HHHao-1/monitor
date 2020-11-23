package com.chaindigg.monitor.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.chaindigg.monitor.vo.AddrRuleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * (addr_rule)数据Mapper
 *
 * @author chenghao
 * @since 2020-11-17 15:33:37
 */
@Mapper
public interface AddrRuleVOMapper extends BaseMapper<AddrRuleVO> {

  @Select(
      "select a.*, b.name "
          + "from addr_rule a join user b "
          + "on a.user_id = b.id ${ew.customSqlSegment}")
  IPage<AddrRuleVO> selectAll(@Param(Constants.WRAPPER) Wrapper wrapper, IPage page);
}
