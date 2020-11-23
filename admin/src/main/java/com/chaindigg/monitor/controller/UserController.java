package com.chaindigg.monitor.controller;

import com.chaindigg.monitor.entity.User;
import com.chaindigg.monitor.service.IUserService;
import com.chaindigg.monitor.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

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
      return apiResponse.response(userService.selectAll(name, currentPage, pageSize));
    } catch (Exception e) {
      e.printStackTrace();
      return apiResponse.fail();
    }
  }

  @PostMapping("/users")
  public ApiResponse<Boolean> addUsers(String name, String phone, String email, String remark) {
    ApiResponse apiResponse = new ApiResponse();
    try {
      return apiResponse.response(userService.add(name, phone, email, remark));
    } catch (Exception e) {
      e.printStackTrace();
      return apiResponse.fail();
    }
  }

  @DeleteMapping("/users")
  public ApiResponse<Boolean> deleteUser(Integer id) {
    ApiResponse apiResponse = new ApiResponse();
    try {
      return apiResponse.response(userService.delete(id));
    } catch (Exception e) {
      e.printStackTrace();
      return apiResponse.fail();
    }
  }

  @PutMapping("/users")
  public ApiResponse<Boolean> updateUser(
      Integer id,
      @Nullable String name,
      @Nullable String phone,
      @Nullable String email,
      @Nullable String remark) {
    ApiResponse apiResponse = new ApiResponse();
    try {
      return apiResponse.response(userService.update(id, name, phone, email, remark));
    } catch (Exception e) {
      e.printStackTrace();
      return apiResponse.fail();
    }
  }
}
