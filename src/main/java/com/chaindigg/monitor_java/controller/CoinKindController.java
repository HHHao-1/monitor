package com.chaindigg.monitor_java.controller;


import com.chaindigg.monitor_java.po.CoinKind;
import com.chaindigg.monitor_java.service.ICoinKindService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 服务控制器
 *
 * @author chenghao
 * @since 2020-11-17 14:46:20
 *
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class CoinKindController {
    private final ICoinKindService coinKindService;

    @GetMapping("/coin-kinds")
    public List<CoinKind> getCoinKindList(){
        return coinKindService.selectAll();
    }

}