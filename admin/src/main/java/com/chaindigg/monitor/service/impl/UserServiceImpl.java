package com.chaindigg.monitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaindigg.monitor.dao.UserMapper;
import com.chaindigg.monitor.entity.User;
import com.chaindigg.monitor.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author chenghao
 * @since 2020-11-17
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

  @Override
  public List<User> selectAll(@Nullable String name, int currentPage, int pageSize) {
    IPage<User> page = new Page<User>(currentPage, pageSize);
    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
    queryWrapper.orderByDesc("id");
    if (!StringUtils.isBlank(name)){
      queryWrapper.eq("name",name);
    }
    return this.page(page, queryWrapper).getRecords();
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

}
