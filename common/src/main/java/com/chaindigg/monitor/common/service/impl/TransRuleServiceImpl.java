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
import com.chaindigg.monitor.common.utils.SearchNoticeWay;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    userQueryWrapper.select("id");
    return userMapper.selectOne(userQueryWrapper).getId();
  }
  
  private List<TransRule> transRuleList(List<Map<String, Object>> list) {
    List<TransRule> listSave = new ArrayList<>();
    list.stream()
        .forEach(
            e -> {
              TransRule transRule = new TransRule();
              transRule
                  .setCoinKind(e.get("eventName").toString())
                  .setNoticeWay(SearchNoticeWay.noticeWayId((String) e.get("noticeWay")))
                  .setMonitorMinVal(e.get("monitorMinVal").toString())
                  .setUserId(Integer.parseInt(e.get("id").toString()))
                  .setState(1)
                  .setEventAddTime(LocalDateTime.now())
                  .setEventUpdateTime(LocalDateTime.now());
              listSave.add(transRule);
            });
    return listSave;
  }
  
  @Override
  public List<TransRule> selectAllById(Integer userId, Integer currentPage, Integer pageSize) {
    IPage<TransRule> page = new Page<TransRule>(currentPage, pageSize);
    QueryWrapper<TransRule> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("user_id", userId).eq("state", 1);
    return this.page(page, queryWrapper).getRecords();
  }
  
  @Override
  public Boolean add(List<Map<String, Object>> list) throws DataBaseException {
    Integer id = searchUserId(list.get(0).get("userName").toString());
    if (id != null) {
      List<TransRule> listSave = transRuleList(list);
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
    Integer id = searchUserId(list.get(0).get("userName").toString());
    if (id != null) {
      List<TransRule> listSave = transRuleList(list);
      return this.updateBatchById(listSave);
    } else {
      throw new DataBaseException(State.USER_NOT_EXIST);
    }
  }
}
