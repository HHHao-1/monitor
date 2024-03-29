package com.chaindigg.monitor.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaindigg.monitor.common.dao.AddrRuleMapper;
import com.chaindigg.monitor.common.dao.UserMapper;
import com.chaindigg.monitor.common.entity.AddrRule;
import com.chaindigg.monitor.common.entity.User;
import com.chaindigg.monitor.common.enums.State;
import com.chaindigg.monitor.common.exception.DataBaseException;
import com.chaindigg.monitor.common.service.IAddrRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AddrRuleServiceImpl extends ServiceImpl<AddrRuleMapper, AddrRule>
    implements IAddrRuleService {
  
  private final UserMapper userMapper;
  private final AddrRuleMapper addrRuleMapper;
  
  @Override
  public Integer searchUserId(String name) {
    QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
    userQueryWrapper.select("id").eq("name", name);
    return userMapper.selectOne(userQueryWrapper).getId();
  }
  
  private List<AddrRule> addrRuleList(List<Map<String, Object>> list, Integer id) {
    List<AddrRule> listSave = new ArrayList<>();
    
    list.stream()
        .forEach(
            e -> {
              AddrRule addrRule = new AddrRule();
              addrRule
                  .setEventName(String.valueOf(e.get("eventName")))
                  .setCoinKind(String.valueOf(e.get("coinKind")))
                  .setAddress(String.valueOf(e.get("address")))
                  .setNoticeWay(Integer.parseInt(String.valueOf(e.get("noticeWay"))))
                  .setMonitorMinVal(Optional.ofNullable(e.get("monitorMinVal")).map(s -> String.valueOf(s)).orElse(null))
                  .setAddressMark(Optional.ofNullable(e.get("addressMark")).map(s -> String.valueOf(s)).orElse(null))
                  .setUserId(id)
                  .setState(1)
                  .setEventAddTime(LocalDateTime.now())
                  .setEventUpdateTime(LocalDateTime.now());
              listSave.add(addrRule);
            });
    return listSave;
  }
  
  private List<AddrRule> updateRuleList(List<Map<String, Object>> list, Integer id) {
    List<AddrRule> listSave = new ArrayList<>();
    list.stream()
        .forEach(
            e -> {
              AddrRule addrRule = new AddrRule();
              addrRule
                  .setId(Integer.parseInt(String.valueOf(e.get("uid"))))
                  .setEventName(String.valueOf(e.get("eventName")))
                  .setCoinKind(String.valueOf(e.get("coinKind")))
                  .setAddress(String.valueOf(e.get("address")))
                  .setNoticeWay(Integer.parseInt(String.valueOf(e.get("noticeWay"))))
                  .setMonitorMinVal(Optional.ofNullable(e.get("monitorMinVal")).map(s -> String.valueOf(s)).orElse(null))
                  .setAddressMark(Optional.ofNullable(e.get("addressMark")).map(s -> String.valueOf(s)).orElse(null))
                  .setState(1)
                  .setEventUpdateTime(LocalDateTime.now());
              if (list.get(0).get("userName") != null) {
                Integer userId = searchUserId(String.valueOf(list.get(0).get("userName")));
                addrRule.setUserId(userId);
              }
              listSave.add(addrRule);
            });
    return listSave;
  }
  
  @Override
  public Map<String, Object> selectAllById(Integer userId, Integer currentPage, Integer pageSize) {
    IPage<AddrRule> page = new Page<AddrRule>(currentPage, pageSize);
    QueryWrapper<AddrRule> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("user_id", userId).eq("state", 1);
    IPage<AddrRule> res = this.page(page, queryWrapper);
    Map<String, Object> map = new HashMap<>();
    map.put("total", res.getTotal());
    map.put("data", res.getRecords());
    return map;
  }
  
  @Override
  public AddrRule selectAllByUId(Integer id) {
    QueryWrapper<AddrRule> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("id", id).eq("state", 1);
    return this.getOne(queryWrapper);
  }
  
  @Override
  public Boolean add(List<Map<String, Object>> list) throws DataBaseException {
    Integer id;
    if (list.get(0).get("userName") != null) {
      id = searchUserId(list.get(0).get("userName").toString());
    } else {
      id = Integer.parseInt(list.get(0).get("id").toString());
    }
    if (id != null) {
      List<AddrRule> listSave = addrRuleList(list, id);
      return this.saveBatch(listSave);
    } else {
      throw new DataBaseException(State.USER_NOT_EXIST);
    }
  }
  
  public Integer addrRuleState(Integer id) {
    QueryWrapper<AddrRule> queryWrapper = new QueryWrapper<>();
    queryWrapper.select("state").eq("id", id);
    return addrRuleMapper.selectOne(queryWrapper).getState();
  }
  
  @Override
  public Boolean delete(Integer id) {
    Integer state = addrRuleState(id);
    UpdateWrapper<AddrRule> queryWrapper = new UpdateWrapper<>();
    queryWrapper.eq("id", id);
    if (state == 0) {
      queryWrapper.set("state", 1);
    } else if (state == 1) {
      queryWrapper.set("state", 0);
    }
    return this.update(queryWrapper);
  }
  
  @Override
  public Boolean update(List<Map<String, Object>> list) throws DataBaseException {
//    Integer id = searchUserId(list.get(0).get("userName").toString());
    Integer id = Integer.parseInt(list.get(0).get("id").toString());
    if (id != null) {
      List<AddrRule> listSave = updateRuleList(list, id);
      return this.updateById(listSave.get(0));
    } else {
      throw new DataBaseException(State.USER_NOT_EXIST);
    }
  }
}
