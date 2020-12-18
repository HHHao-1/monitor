package com.chaindigg.monitor.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.chaindigg.monitor.admin.DingTalkProvider;
import com.chaindigg.monitor.admin.service.DingTalk;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

@Service
@Slf4j
public class DDTalkImpl implements DingTalk {
  @Resource
  private DingTalkProvider dingTalkProvider;
  
  public Object getInfo(@NotNull String code, @RequestParam String state) {
    Object ob = dingTalkProvider.loginOrRegister(code, state);
    log.info(ob.toString());
    return ob;
  }
  
  public Object manage(String unionId, Integer apply) {
    String state = "monitor";
    String authorization = JSON.toJSONString("");
    String role = "";
    return dingTalkProvider.user_permission(unionId, state, apply, authorization, role);
  }
  
  public Object userList() {
    return dingTalkProvider.user_list("monitor");
  }
}
