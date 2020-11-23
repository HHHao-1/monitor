// package com.chaindigg.monitor.controller;
//
//
// import com.chaindigg.monitor.vo.MonitorTransVO;
// import com.chaindigg.monitor.service.IMonitorTransVOService;
// import lombok.RequiredArgsConstructor;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RestController;
//
// import java.util.List;
//
/// **
// * 服务控制器
// *
// * @author chenghao
// * @since 2020-11-16 17:40:50
// */
//
// @RequiredArgsConstructor
// @RestController
// public class MonitorTransController {
//  private final IMonitorTransVOService monitorTransService;
//
//  @GetMapping("/monitor-transactions/{id}")
//  public List<MonitorTransVO> getMonitorTransListByUserId(@PathVariable String id, int
// currentPage, int pageSize) {
//    return monitorTransService.selectByUserId(id, currentPage, pageSize);
//  }
//
//  @GetMapping("/monitor-transactions")
//  public List<MonitorTransVO> getMonitorTransList(int currentPage, int pageSize) {
//    return monitorTransService.selectAll(currentPage, pageSize);
//  }
//
// }
