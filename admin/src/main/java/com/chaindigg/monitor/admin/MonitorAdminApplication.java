package com.chaindigg.monitor.admin;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication(
    scanBasePackages = {"com.chaindigg.monitor.admin", "com.chaindigg.monitor.common"})
@MapperScan(basePackages = {"com.chaindigg.monitor.admin.dao", "com.chaindigg.monitor.common.dao"})
public class MonitorAdminApplication {
  public static void main(String[] args) {
    log.info("线程：" + Thread.currentThread().getName());
    SpringApplication.run(MonitorAdminApplication.class, args);
  }
}
