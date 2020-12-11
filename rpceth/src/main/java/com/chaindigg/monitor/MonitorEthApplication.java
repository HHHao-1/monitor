package com.chaindigg.monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MonitorEthApplication {
  
  public static void main(String[] args) {
    SpringApplication.run(MonitorEthApplication.class, args);
  }
  
}
