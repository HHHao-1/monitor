package com.chaindigg.monitor.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.chaindigg.monitor.dao")
public class MonitorAdminApplication {

  public static void main(String[] args) {
    SpringApplication.run(MonitorAdminApplication.class, args);
  }
}
