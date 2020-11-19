//package com.chaindigg.monitor.dao;
//
//
//import com.baomidou.mybatisplus.core.mapper.BaseMapper;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import org.apache.ibatis.annotations.Mapper;
//import org.apache.ibatis.annotations.Select;
//
//import java.util.List;
//
///**
// * (addr_rule)数据Mapper
// *
// * @author chenghao
// * @since 2020-11-17 15:33:37
// */
//@Mapper
//public interface AddrRuleMapper extends BaseMapper<AddrRuleVO> {
//  @Select("select a.*, b.name user_name " +
//      "from addr_rule a join user b " +
//      "on a.user_id = b.id " +
//      "where a.user_id = #{id} " +
//      "order by event_add_time desc")
//  List<AddrRuleVO> selectByUserId(IPage page, String id);
//
//  @Select("select a.*, b.name user_name " +
//      "from addr_rule a join user b " +
//      "on a.user_id = b.id " +
//      "order by event_add_time desc")
//  List<AddrRuleVO> selectAll(IPage page);
//}
