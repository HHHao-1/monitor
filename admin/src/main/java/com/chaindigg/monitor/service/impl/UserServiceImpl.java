package com.chaindigg.monitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaindigg.monitor.dao.UserMapper;
import com.chaindigg.monitor.entity.User;
import com.chaindigg.monitor.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

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
  public List<User> selectAll(int currentPage, int pageSize) {
    IPage<User> page = new Page<User>(currentPage, pageSize);
    LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.orderByDesc(User::getCreateTime);
    return this.page(page, queryWrapper).getRecords();
  }

  @Override
  public Boolean add(User... user) {
    return this.saveBatch(Arrays.asList(user));
  }
}
