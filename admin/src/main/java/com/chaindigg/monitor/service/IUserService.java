package com.chaindigg.monitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaindigg.monitor.entity.User;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author chenghao
 * @since 2020-11-17
 */
public interface IUserService extends IService<User> {
  List<User> selectAll(int currentPage, int pageSize);

  Boolean add(User... user);
}
