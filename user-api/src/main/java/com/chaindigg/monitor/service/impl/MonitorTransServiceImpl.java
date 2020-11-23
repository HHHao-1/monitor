//package com.chaindigg.monitor.service.impl;
//
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.chaindigg.monitor.dao.MonitorTransMapper;
//import com.chaindigg.monitor.entity.TransRule;
//import com.chaindigg.monitor.vo.MonitorTransVO;
//import com.chaindigg.monitor.entity.MonitorTrans;
//import com.chaindigg.monitor.service.IMonitorTransService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
///**
// * <p>
// * 服务实现类
// * </p>
// *
// * @author chenghao
// * @since 2020-11-17
// */
//@Service
//@RequiredArgsConstructor
//public class MonitorTransServiceImpl extends ServiceImpl<MonitorTransMapper, MonitorTrans> implements IMonitorTransService {
//
//  private final MonitorTransMapper monitorTransMapper;
//
//
//  @Override
//  public List<MonitorTransVO> selectByUserId(String id, int currentPage, int pageSize) {
//    IPage<TransRule> page = new Page<TransRule>(currentPage, pageSize);
//    return monitorTransMapper.selectByUserId(page, id);
//  }
//
//  @Override
//  public List<MonitorTransVO> selectAll(int currentPage, int pageSize) {
//    IPage<TransRule> page = new Page<TransRule>(currentPage, pageSize);
//    return monitorTransMapper.selectAll(page);
//  }
//}
