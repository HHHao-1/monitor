package com.chaindigg.monitor.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaindigg.monitor.common.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {}
