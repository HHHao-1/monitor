package com.chaindigg.monitor_java.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaindigg.monitor_java.dao.UserMapper;
import com.chaindigg.monitor_java.po.User;
import com.chaindigg.monitor_java.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chenghao
 * @since 2020-11-17
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

  private final UserMapper userMapper;

  @Override
  public List<User> selectAll() {
    return userMapper.selectList(null);
  }

  @Override
  public Boolean add(User... user) {
    return this.saveBatch(Arrays.asList(user));
  }
}
