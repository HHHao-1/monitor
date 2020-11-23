//package com.chaindigg.monitor.dao;
//
//import com.baomidou.mybatisplus.core.mapper.BaseMapper;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.chaindigg.monitor.vo.MonitorTransVO;
//import com.chaindigg.monitor.entity.MonitorTrans;
//import org.apache.ibatis.annotations.Mapper;
//import org.apache.ibatis.annotations.Select;
//
//import java.util.List;
//
///**
// * (monitor_trans)数据Mapper
// *
// * @author chenghao
// * @since 2020-11-16 17:40:50
// */
//@Mapper
//public interface MonitorTransMapper extends BaseMapper<MonitorTrans> {
//
//  @Select("select a.trans_hash, a.from_address, a.to_address, a.unusual_count, a.unusual_time, b.coin_kind " +
//      "from monitor_trans a join trans_rule b " +
//      "on a.trans_rule_id = b.id " +
//      "where b.user_id = #{id} " +
//      "order by notice_time desc")
//  List<MonitorTransVO> selectByUserId(IPage page, String id);
//
//  @Select("select a.trans_hash, a.from_address, a.to_address, a.unusual_count, a.unusual_time, b.coin_kind " +
//      "from monitor_trans a join trans_rule b " +
//      "on a.trans_rule_id = b.id " +
//      "order by notice_time desc")
//  List<MonitorTransVO> selectAll(IPage page);
//
//}
