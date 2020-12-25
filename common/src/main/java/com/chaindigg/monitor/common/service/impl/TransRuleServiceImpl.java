package com.chaindigg.monitor.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaindigg.monitor.common.dao.TransRuleMapper;
import com.chaindigg.monitor.common.dao.UserMapper;
import com.chaindigg.monitor.common.entity.TransRule;
import com.chaindigg.monitor.common.entity.User;
import com.chaindigg.monitor.common.enums.State;
import com.chaindigg.monitor.common.exception.DataBaseException;
import com.chaindigg.monitor.common.service.ITransRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransRuleServiceImpl extends ServiceImpl<TransRuleMapper, TransRule>
    implements ITransRuleService {
  
  private final UserMapper userMapper;
  private final TransRuleMapper transRuleMapper;
  
  public Integer searchUserId(String name) {
    QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
    userQueryWrapper.select("id").eq("name", name);
    return userMapper.selectOne(userQueryWrapper).getId();
  }
  
  private List<TransRule> transRuleList(List<Map<String, Object>> list, Integer id) {
    List<TransRule> listSave = new ArrayList<>();
    list.stream()
        .forEach(
            e -> {
              TransRule transRule = new TransRule();
              transRule
                  .setCoinKind(String.valueOf(e.get("coinKind")))
                  .setNoticeWay(Integer.parseInt(String.valueOf(e.get("noticeWay"))))
                  .setMonitorMinVal(String.valueOf(e.get("monitorMinVal")))
                  .setUserId(id)
                  .setState(1)
                  .setEventAddTime(LocalDateTime.now())
                  .setEventUpdateTime(LocalDateTime.now());
              listSave.add(transRule);
            });
    return listSave;
  }
  
  private List<TransRule> updateRuleList(List<Map<String, Object>> list, Integer id) {
    List<TransRule> listSave = new ArrayList<>();
    list.stream()
        .forEach(
            e -> {
              TransRule transRule = new TransRule();
              transRule
                  .setId(Integer.parseInt(String.valueOf(e.get("uid"))))
                  .setCoinKind(String.valueOf(e.get("coinKind")))
                  .setNoticeWay(Integer.parseInt(String.valueOf(e.get("noticeWay"))))
                  .setMonitorMinVal(String.valueOf(e.get("monitorMinVal")))
                  .setState(1)
                  .setEventUpdateTime(LocalDateTime.now());
              if (list.get(0).get("userName") != null) {
                Integer userId = searchUserId(String.valueOf(list.get(0).get("userName")));
                transRule.setUserId(userId);
              }
              listSave.add(transRule);
            });
    return listSave;
  }
  
  @Override
  public Map<String, Object> selectAllById(Integer userId, Integer currentPage, Integer pageSize) {
    IPage<TransRule> page = new Page<TransRule>(currentPage, pageSize);
    QueryWrapper<TransRule> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("user_id", userId).eq("state", 1);
    IPage<TransRule> res = this.page(page, queryWrapper);
    Map<String, Object> map = new HashMap<>();
    map.put("total", res.getTotal());
    map.put("data", res.getRecords());
    return map;
  }
  
  @Override
  public TransRule selectAllByUId(Integer Id) {
    QueryWrapper<TransRule> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("id", Id).eq("state", 1);
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
//    Integer id = searchUserId(list.get(0).get("userName").toString());
//    Integer id = Integer.parseInt(list.get(0).get("id").toString());
    if (id != null) {
      List<TransRule> listSave = transRuleList(list, id);
      return this.saveBatch(listSave);
    } else {
      throw new DataBaseException(State.USER_NOT_EXIST);
    }
  }
  
  public Integer transRuleState(Integer id) {
    QueryWrapper<TransRule> queryWrapper = new QueryWrapper<>();
    queryWrapper.select("state").eq("id", id);
    return transRuleMapper.selectOne(queryWrapper).getState();
  }
  
  @Override
  public Boolean delete(Integer id) {
    Integer state = transRuleState(id);
    UpdateWrapper<TransRule> queryWrapper = new UpdateWrapper<>();
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
      List<TransRule> listSave = updateRuleList(list, id);
      return this.updateById(listSave.get(0));
    } else {
      throw new DataBaseException(State.USER_NOT_EXIST);
    }
  }
}
