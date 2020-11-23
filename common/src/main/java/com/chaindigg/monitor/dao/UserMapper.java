package com.chaindigg.monitor.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaindigg.monitor.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {}
