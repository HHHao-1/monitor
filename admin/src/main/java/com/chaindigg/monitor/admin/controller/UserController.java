package com.chaindigg.monitor.admin.controller;

import com.chaindigg.monitor.admin.service.IUserService;
import com.chaindigg.monitor.common.enums.State;
import com.chaindigg.monitor.common.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserController {
  private final IUserService userService;
  
  @GetMapping("/users")
  public ApiResponse getUsers(String name, Integer currentPage, Integer pageSize) {
    try {
      return ApiResponse.create(State.SUCCESS, userService.selectAll(name, currentPage, pageSize));
    } catch (Exception e) {
      e.printStackTrace();
      return ApiResponse.create(State.FAIL);
    }
  }
  
  @GetMapping("/users/list")
  public ApiResponse getUserList() {
    try {
      return ApiResponse.create(State.SUCCESS, userService.selectList());
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
      return ApiResponse.create(State.SUCCESS, userService.delete(id), State.SUCCESS);
    } catch (Exception e) {
      e.printStackTrace();
      return ApiResponse.create(State.FAIL);
    }
  }
  
  @PutMapping("/users")
  public ApiResponse updateUser(
      Integer id, String name, String phone, String email, String remark) {
    try {
      return ApiResponse.create(State.SUCCESS, userService.update(id, name, phone, email, remark));
    } catch (Exception e) {
      e.printStackTrace();
      return ApiResponse.create(State.FAIL);
    }
  }
}
