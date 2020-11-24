package com.chaindigg.monitor.admin.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.chaindigg.monitor.admin.vo.TransRuleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * (trans_rule)数据Mapper
 *
 * @author chenghao
 * @since 2020-11-16 17:40:50
 */
@Mapper
public interface TransRuleVOMapper extends BaseMapper<TransRuleVO> {

  @Select(
      "select a.*, b.name "
          + "from trans_rule a join user b "
          + "on a.user_id = b.id ${ew.customSqlSegment}")
  IPage<TransRuleVO> selectAll(@Param(Constants.WRAPPER) Wrapper wrapper, IPage page);
}
