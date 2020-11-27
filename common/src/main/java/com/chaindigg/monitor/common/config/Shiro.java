// package com.chaindigg.monitor.common.config;
//
// import com.chaindigg.monitor.common.shiro.MyShiroRealm;
// import org.apache.shiro.realm.Realm;
// import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
// import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
//
// public class Shiro {
//  @Configuration
//  public class ShiroConfig {
//
//    @Bean
//    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
//      DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
//
//      return chainDefinition;
//    }
//
//    @Bean
//    public Realm myShiroRealm() {
//      MyShiroRealm realm = new MyShiroRealm();
//      return realm;
//    }
//  }
// }
