package com.chaindigg.monitor.admin.service;

import org.springframework.web.bind.annotation.RequestParam;

public interface DingTalk {
  Object getInfo(String code, @RequestParam String state);
  
  Object manage(String unionId, Integer apply);
  
  Object userList();
}
