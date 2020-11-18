package com.chaindigg.monitor_java.controller;

import com.chaindigg.monitor_java.po.User;
import com.chaindigg.monitor_java.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 服务控制器
 *
 * @author chenghao
 * @since 2020-11-16 17:40:50
 *
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {
    private final IUserService userService;

    @GetMapping("/users")
    public List<User> getUserList() {
        return userService.selectAll();
    }

    @PostMapping("/users")
    public Boolean addUsers(User ...user){
        return userService.add(user);
    }

}