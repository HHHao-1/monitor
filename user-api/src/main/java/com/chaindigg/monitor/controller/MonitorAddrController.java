//package com.chaindigg.monitor.controller;
//
//
//import com.chaindigg.monitor.vo.MonitorAddrVO;
//import com.chaindigg.monitor.service.IMonitorAddrService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
///**
// * 服务控制器
// *
// * @author chenghao
// * @since 2020-11-16 17:40:50
// */
//
//@RequiredArgsConstructor
//@RestController
//public class MonitorAddrController {
//  private final IMonitorAddrService monitorAddrService;
//
//  @GetMapping("/monitor-addresses/{id}")
//  public List<MonitorAddrVO> getMonitorAddrListByUserId(@PathVariable String id, int currentPage, int pageSize) {
//    return monitorAddrService.selectByUserId(id, currentPage, pageSize);
//  }
//
//  @GetMapping("/monitor-addresses")
//  public List<MonitorAddrVO> getMonitorAddrList(int currentPage, int pageSize) {
//    return monitorAddrService.selectAll(currentPage, pageSize);
//  }
//
//}