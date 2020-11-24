package com.chaindigg.monitor.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaindigg.monitor.common.entity.User;

import java.util.List;

/**
 * 服务类
 *
 * @author chenghao
 * @since 2020-11-17
 */
public interface IUserService extends IService<User> {

  List<User> selectAll(String name, int currentPage, int pageSize);

  Boolean add(String name, String phone, String email, String remark);

  Boolean delete(Integer id);

  Boolean update(Integer id, String name, String phone, String email, String remark);
}
