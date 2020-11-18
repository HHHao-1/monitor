package com.chaindigg.monitor_java.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaindigg.monitor_java.po.AddrRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * (addr_rule)数据Mapper
 *
 * @author chenghao
 * @since 2020-11-17 15:33:37
 *
*/
@Mapper
public interface AddrRuleMapper extends BaseMapper<AddrRule> {
  @Select("select a.*, b.name " +
      "from addr_rule a join user b " +
      "on a.user_id = b.id " +
      "where a.user_id = #{id}")
  List<AddrRule> selectByUserId(String id);

  @Select("select a.*, b.name " +
      "from addr_rule a join user b " +
      "on a.user_id = b.id")
  List<AddrRule> selectAll();
}
