package com.chaindigg.monitor_java;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.chaindigg.monitor_java.dao")
public class MonitorJavaApplication {

  public static void main(String[] args) {
    SpringApplication.run(MonitorJavaApplication.class, args);
  }

}
