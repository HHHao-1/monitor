package com.chaindigg.monitor.admin.controller;

import com.chaindigg.monitor.admin.service.DingTalk;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

@RestController
public class DingTalkController {
  
  @Resource
  private DingTalk dingTalk;
  
  @GetMapping("/ding/login")
  Object getInfo(@RequestParam @NotNull String code, @RequestParam String state) {
    return dingTalk.getInfo(code, state);
  }
  
  @PostMapping("/ding/{unionId}")
  Object manage(@PathVariable String unionId, @RequestParam Integer apply) {
    return dingTalk.manage(unionId, apply);
  }
  
  @GetMapping("/ding/user-list")
  Object userList() {
    return dingTalk.userList();
  }
}
