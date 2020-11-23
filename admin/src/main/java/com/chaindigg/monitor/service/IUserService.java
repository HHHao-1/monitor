package com.chaindigg.monitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaindigg.monitor.entity.User;
import org.springframework.lang.Nullable;

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

  List<User> selectAll(@Nullable String name, int currentPage, int pageSize);

  Boolean add(String name, String phone, String email, String remark);

  Boolean delete(Integer id);

  Boolean update(Integer id, @Nullable String name, @Nullable String phone, @Nullable String email, @Nullable String remark);

}
