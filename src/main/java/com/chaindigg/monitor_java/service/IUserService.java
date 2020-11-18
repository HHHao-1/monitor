package com.chaindigg.monitor_java.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaindigg.monitor_java.po.User;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chenghao
 * @since 2020-11-17
 */
public interface IUserService extends IService<User> {
  List<User> selectAll();
  Boolean add(User ...user);
}
