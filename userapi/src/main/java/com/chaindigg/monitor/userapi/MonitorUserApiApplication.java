package com.chaindigg.monitor.userapi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
    scanBasePackages = {"com.chaindigg.monitor.userapi", "com.chaindigg.monitor.common"})
@MapperScan({"com.chaindigg.monitor.userapi.dao", "com.chaindigg.monitor.common.dao"})
public class MonitorUserApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(MonitorUserApiApplication.class, args);
  }
}
