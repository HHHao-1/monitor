package com.chaindigg.monitor.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaindigg.monitor.common.entity.User;

import java.util.Map;

/**
 * 服务类
 *
 * @author chenghao
 * @since 2020-11-17
 */
public interface IUserService extends IService<User> {
  
  Map<String, Object> selectAll(String name, Integer currentPage, Integer pageSize);
  
  Map<String, Object> selectList();
  
  Boolean add(String name, String phone, String email, String remark);
  
  Boolean delete(Integer id);
  
  Boolean update(Integer id, String name, String phone, String email, String remark);
}
