package com.chaindigg.monitor.controller;

import com.chaindigg.monitor.entity.User;
import com.chaindigg.monitor.enums.State;
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
  public ApiResponse getUsers(@Nullable String name, int currentPage, int pageSize) {
    try {
      return ApiResponse.create(State.SUCCESS, userService.selectAll(name, currentPage, pageSize));
    } catch (Exception e) {
      e.printStackTrace();
      return ApiResponse.create(State.FAIL);
    }
  }

  @PostMapping("/users")
  public ApiResponse addUsers(String name, String phone, String email, String remark) {

    try {
      return ApiResponse.create(State.SUCCESS, userService.add(name, phone, email, remark));
    } catch (Exception e) {
      e.printStackTrace();
      return ApiResponse.create(State.FAIL);
    }
  }

  @DeleteMapping("/users")
  public ApiResponse deleteUser(Integer id) {
    try {
      return ApiResponse.create(State.SUCCESS, userService.delete(id),State.SUCCESS);
    } catch (Exception e) {
      e.printStackTrace();
      return ApiResponse.create(State.FAIL);
    }
  }

  @PutMapping("/users")
  public ApiResponse updateUser(
      Integer id,
      @Nullable String name,
      @Nullable String phone,
      @Nullable String email,
      @Nullable String remark) {

    try {
      return ApiResponse.create(State.SUCCESS, userService.update(id, name, phone, email, remark));
    } catch (Exception e) {
      e.printStackTrace();
      return ApiResponse.create(State.FAIL);
    }
  }
}
