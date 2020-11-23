//package com.chaindigg.monitor.controller;
//
//
//import com.chaindigg.monitor.entity.AddrRule;
//import com.chaindigg.monitor.service.IAddrRuleService;
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
//public class AddrRuleController {
//  private final IAddrRuleService addrRuleService;
//
//  @GetMapping("/addr-rules/{id}")
//  public List<AddrRule> getAllRulesListByUserId(@PathVariable String id, int currentPage, int pageSize) {
//    return addrRuleService.selectByUserId(id, currentPage, pageSize);
//  }
//
//}