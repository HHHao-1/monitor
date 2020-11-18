package com.chaindigg.monitor_java.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaindigg.monitor_java.po.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * (user)数据Mapper
 *
 * @author chenghao
 * @since 2020-11-16 17:40:50
 *
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
