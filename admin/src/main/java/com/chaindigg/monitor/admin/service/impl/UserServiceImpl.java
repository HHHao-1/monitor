package com.chaindigg.monitor.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaindigg.monitor.admin.service.IUserService;
import com.chaindigg.monitor.common.dao.UserMapper;
import com.chaindigg.monitor.common.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
  
  private final UserMapper userMapper;
  
  @Override
  public Map<String, Object> selectAll(String name, Integer currentPage, Integer pageSize) {
    IPage<User> page = new Page<User>(currentPage, pageSize);
    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
    queryWrapper.orderByDesc("id");
    if (!StringUtils.isBlank(name)) {
      queryWrapper.eq("name", name);
    }
    IPage<User> res = this.page(page, queryWrapper);
    Map<String, Object> map = new HashMap<>();
    map.put("total", res.getTotal());
    map.put("data", res.getRecords());
    return map;
  }
  
  @Override
  public Boolean add(String name, String phone, String email, String remark) {
    User user = new User();
    user.setName(name)
        .setPhone(phone)
        .setEmail(email)
        .setRemark(remark)
        .setCreateTime(LocalDateTime.now())
        .setUpdateTime(LocalDateTime.now())
        .setState(1);
    return this.save(user);
  }
  
  public Integer userState(Integer id) {
    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
    queryWrapper.select("state").eq("id", id);
    return userMapper.selectOne(queryWrapper).getState();
  }
  
  @Override
  public Boolean delete(Integer id) {
    Integer state = userState(id);
    UpdateWrapper<User> queryWrapper = new UpdateWrapper<>();
    queryWrapper.eq("id", id);
    if (state == 0) {
      queryWrapper.set("state", 1);
      queryWrapper.set("update_time", LocalDateTime.now());
    } else if (state == 1) {
      queryWrapper.set("state", 0);
      queryWrapper.set("update_time", LocalDateTime.now());
    }
    return this.update(queryWrapper);
  }
  
  @Override
  public Boolean update(Integer id, String name, String phone, String email, String remark) {
    UpdateWrapper<User> queryWrapper = new UpdateWrapper<>();
    queryWrapper.eq("id", id);
    if (!StringUtils.isBlank(name)) {
      queryWrapper.set("name", name);
      queryWrapper.set("update_time", LocalDateTime.now());
    }
    if (!StringUtils.isBlank(phone)) {
      queryWrapper.set("phone", phone);
      queryWrapper.set("update_time", LocalDateTime.now());
    }
    if (!StringUtils.isBlank(email)) {
      queryWrapper.set("email", email);
      queryWrapper.set("update_time", LocalDateTime.now());
    }
    if (!StringUtils.isBlank(remark)) {
      queryWrapper.set("remark", remark);
      queryWrapper.set("update_time", LocalDateTime.now());
    }
    return this.update(queryWrapper);
  }
}
