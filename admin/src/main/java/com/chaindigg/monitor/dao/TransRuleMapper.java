package com.chaindigg.monitor.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chaindigg.monitor.vo.TransRuleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * (trans_rule)数据Mapper
 *
 * @author chenghao
 * @since 2020-11-16 17:40:50
 */
@Mapper
public interface TransRuleMapper extends BaseMapper<TransRuleVO> {

  @Select("select a.*, b.name " +
      "from trans_rule a join user b " +
      "on a.user_id = b.id ")
  IPage<TransRuleVO> selectAll(@Nullable Wrapper wrapper, IPage page);

}
