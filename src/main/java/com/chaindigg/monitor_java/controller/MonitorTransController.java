package com.chaindigg.monitor_java.controller;


import com.chaindigg.monitor_java.dto.MonitorTransDTO;
import com.chaindigg.monitor_java.service.IMonitorTransService;
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
public class MonitorTransController {
    private final IMonitorTransService monitorTransService;

    @GetMapping("/monitor-transactions/{id}")
    public List<MonitorTransDTO> getMonitorTransListByUserId(@PathVariable String id){
        return monitorTransService.selectByUserId(id);
    }

    @GetMapping("/monitor-transactions")
    public List<MonitorTransDTO> getMonitorTransList (){
        return monitorTransService.selectAll();
    }

}