package com.chaindigg.monitor_java.controller;


import com.chaindigg.monitor_java.dto.MonitorAddrDTO;
import com.chaindigg.monitor_java.service.IMonitorAddrService;
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
public class MonitorAddrController {
    private final IMonitorAddrService monitorAddrService;

    @GetMapping("/monitor-addresses/{id}")
    public List<MonitorAddrDTO> getMonitorAddrListByUserId(@PathVariable String id){
        return monitorAddrService.selectByUserId(id);
    }

    @GetMapping("/monitor-addresses")
    public List<MonitorAddrDTO> getMonitorAddrList (){
        return monitorAddrService.selectAll();
    }

}