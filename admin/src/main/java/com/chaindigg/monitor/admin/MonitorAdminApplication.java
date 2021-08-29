package com.chaindigg.monitor.admin;

import lombok.extern.slf4j.Slf4j;

import org.apache.catalina.core.ApplicationContext;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication(
    scanBasePackages = {"com.chaindigg.monitor.admin", "com.chaindigg.monitor.common"})
@MapperScan(basePackages = {"com.chaindigg.monitor.admin.dao", "com.chaindigg.monitor.common.dao"})
@EnableDiscoveryClient
@EnableFeignClients
public class MonitorAdminApplication {
  public static void main(String[] args) {
    SpringApplication.run(MonitorAdminApplication.class, args);
  }
  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      caller.rightWayToCall();
      Thread.sleep(1000);
      System.out.println("-----");
      Thread.sleep(1000);
      caller.wrongWayToCall();
    };
}
