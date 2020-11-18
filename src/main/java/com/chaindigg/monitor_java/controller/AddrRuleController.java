package com.chaindigg.monitor_java.controller;


import com.chaindigg.monitor_java.po.AddrRule;
import com.chaindigg.monitor_java.service.IAddrRuleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 服务控制器
 *
 * @author chenghao
 * @since 2020-11-16 17:40:50
 *
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class AddrRuleController {
    private final IAddrRuleService addrRuleService;

    @GetMapping("/addr-rules/{id}")
    public List<AddrRule> getAllRulesListByUserId(@PathVariable String id) {
        return addrRuleService.selectByUserId(id);
    }

    @GetMapping("/addr-rules")
    public List<AddrRule> getAllRulesList() {
        return addrRuleService.selectAll();
    }

}