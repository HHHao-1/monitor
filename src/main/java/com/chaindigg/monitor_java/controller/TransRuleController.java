package com.chaindigg.monitor_java.controller;


import com.chaindigg.monitor_java.po.TransRule;
import com.chaindigg.monitor_java.service.ITransRuleService;
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
public class TransRuleController {
    private final ITransRuleService transRuleService;

    @GetMapping("/transaction-rules/{id}")
    public List<TransRule> getAllRulesListByUserId(@PathVariable String id) {
        return transRuleService.selectByUserId(id);
    }

    @GetMapping("/transaction-rules")
    public List<TransRule> getAllRulesList() {
        return transRuleService.selectAll();
    }

}