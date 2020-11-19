package com.chaindigg.monitor.controller;

import com.chaindigg.monitor.entity.User;
import com.chaindigg.monitor.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 服务控制器
 *
 * @author chenghao
 * @since 2020-11-16 17:40:50
 */

@RequiredArgsConstructor
@RestController
public class UserController {
  private final IUserService userService;

  @GetMapping("/users")
  public List<User> getUsers(@Nullable String name, int currentPage, int pageSize) {
    return userService.selectAll(name, currentPage, pageSize);
  }

  @PostMapping("/users")
  public Boolean addUsers(User... user) {
    return userService.add(user);
  }

}