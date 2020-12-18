package com.chaindigg.monitor.admin;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@FeignClient(value = "dingtalk-provider")
public interface DingTalkProvider {
  
  @RequestMapping(value = "/api/testconnection")
  String testCpnnection();
  
  @RequestMapping(value = "/api/login", method = RequestMethod.GET)
  Object loginOrRegister(@RequestParam @NotNull String code, @RequestParam String state);
  
  @RequestMapping(value = "/api/platform", method = RequestMethod.PUT)
  Object platform_add(@RequestParam @NotNull String state, @RequestParam @NotNull String name,
                      @RequestParam @NotNull String url);
  
  @RequestMapping(value = "/api/platform", method = RequestMethod.GET)
  Object platform_list();
  
  @RequestMapping(value = "api/platform/{state}", method = RequestMethod.GET)
  Object platform_get(@PathVariable String state);
  
  @RequestMapping(value = "api/platform/{state}", method = RequestMethod.DELETE)
  Object platform_delete(@PathVariable String state);
  
  @RequestMapping(value = "api/user", method = RequestMethod.GET)
  Object user_list(@RequestParam @NotNull String state);
  
  @RequestMapping(value = "api/user/{unionId}/permission", method = RequestMethod.POST)
  Object user_permission(@PathVariable String unionId, @RequestParam @NotNull String state, @RequestParam Integer apply,
                         @RequestBody String authorization, @RequestParam String role);
  
  @RequestMapping(value = "api/user/{unionId}", method = RequestMethod.DELETE)
  Object user_delete(@PathVariable String unionId, @RequestParam @NotNull String state);
  
  @RequestMapping(value = "api/message/all/text", method = RequestMethod.POST)
  Object message_sendToAllByText(@RequestBody @NotNull String content);
  
  @RequestMapping(value = "api/message/all/link", method = RequestMethod.POST)
  Object message_sendToAllByLink(@RequestParam @NotNull String title, @RequestBody @NotNull String text,
                                 @RequestParam @NotNull String url);
  
  @RequestMapping(value = "api/message/department/text", method = RequestMethod.POST)
  Object message_sendToDepartmentByText(@RequestParam @NotNull String departments, @RequestBody @NotNull String content);
  
  @RequestMapping(value = "api/message/department/link", method = RequestMethod.POST)
  Object message_sendToDepartmentByLink(@RequestParam @NotNull String departments, @RequestParam @NotNull String title,
                                        @RequestBody @NotNull String text, @RequestParam @NotNull String url);
  
  @RequestMapping(value = "api/message/user/text", method = RequestMethod.POST)
  Object message_sendToUserByText(@RequestParam @NotNull String users, @RequestBody @NotNull String content);
  
  @RequestMapping(value = "api/message/user/link", method = RequestMethod.POST)
  Object message_sendToUserByLink(@RequestParam @NotNull String users, @RequestParam @NotNull String title, @RequestBody @NotNull String text, @RequestParam @NotNull String url);
  
  @RequestMapping(value = "api/user/getallusers", method = RequestMethod.GET)
  Object user_getAllUsers();
  
  @RequestMapping(value = "api/user/getalldepartments", method = RequestMethod.GET)
  Object user_getAllDepartments();
  
  
}
