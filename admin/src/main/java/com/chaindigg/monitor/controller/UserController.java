package com.chaindigg.monitor.controller;

import com.chaindigg.monitor.entity.User;
import com.chaindigg.monitor.service.IUserService;
import com.chaindigg.monitor.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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
  public ApiResponse<User> getUsers(@Nullable String name, int currentPage, int pageSize) {
    ApiResponse apiResponse = new ApiResponse();
    try {
      return apiResponse.success(userService.selectAll(name, currentPage, pageSize));
    }catch (Exception e){
      e.printStackTrace();
      return apiResponse.fail();
    }
  }

  @PostMapping("/users")
  public ApiResponse<Boolean> addUsers(String name, String phone, String email, String remark) {
    ApiResponse apiResponse = new ApiResponse();
    try {
      return apiResponse.success(userService.add(name, phone, email, remark));
    } catch (Exception e) {
      e.printStackTrace();
      return apiResponse.fail();
    }
  }
}