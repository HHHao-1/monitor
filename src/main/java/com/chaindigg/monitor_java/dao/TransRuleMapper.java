package com.chaindigg.monitor_java.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaindigg.monitor_java.po.TransRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * (trans_rule)数据Mapper
 *
 * @author chenghao
 * @since 2020-11-16 17:40:50
 *
*/
@Mapper
public interface TransRuleMapper extends BaseMapper<TransRule> {

  @Select("select a.*, b.name " +
      "from trans_rule a join user b " +
      "on a.user_id = b.id where a.user_id = #{id}")
  List<TransRule> selectByUserId(String id);

  @Select("select a.*, b.name " +
      "from trans_rule a join user b " +
      "on a.user_id = b.id")
  List<TransRule> selectAll();
}
