package com.chaindigg.monitor.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
    scanBasePackages = {"com.chaindigg.monitor.admin", "com.chaindigg.monitor.common"})
@MapperScan(basePackages = {"com.chaindigg.monitor.admin.dao", "com.chaindigg.monitor.common.dao"})
public class MonitorAdminApplication {
  public static void main(String[] args) {
    SpringApplication.run(MonitorAdminApplication.class, args);
  }
}
