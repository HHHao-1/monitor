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
import com.chaindigg.monitor.common.utils.SearchNoticeWay;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
                  .setEventName((String) e.get("eventName"))
                  .setCoinKind(e.get("coinKind").toString())
                  .setAddress(e.get("address").toString())
                  .setNoticeWay(SearchNoticeWay.noticeWayId((String) e.get("noticeWay")))
                  .setMonitorMinVal((String) e.get("monitorMinVal"))
                  .setAddressMark((String) e.get("addressMark"))
                  .setUserId(id)
                  .setState(1)
                  .setEventAddTime(LocalDateTime.now())
                  .setEventUpdateTime(LocalDateTime.now());
              listSave.add(addrRule);
            });
    return listSave;
  }

  @Override
  public List<AddrRule> selectAllById(Integer id, int currentPage, int pageSize) {
    IPage<AddrRule> page = new Page<AddrRule>(currentPage, pageSize);
    QueryWrapper<AddrRule> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("id", id).eq("state", 1);
    return this.page(page, queryWrapper).getRecords();
  }

  @Override
  public Boolean add(List<Map<String, Object>> list) throws DataBaseException {
    Integer id = searchUserId(list.get(0).get("userName").toString());
    if (id != null) {
      List<AddrRule> listSave = addrRuleList(list, id);
      return this.saveBatch(listSave);
    } else {
      throw new DataBaseException(State.USER_NOT_EXIST);
    }
  }

  public Integer addrRuleState(String userName, String eventName, LocalDateTime addTime) {
    QueryWrapper<AddrRule> queryWrapper = new QueryWrapper<>();
    queryWrapper
        .select("state")
        .eq("id", userName)
        .eq("event_name", eventName)
        .eq("event_add_time", addTime);
    return addrRuleMapper.selectOne(queryWrapper).getState();
  }

  @Override
  public Boolean delete(String userName, String eventName, LocalDateTime addTime) {
    Integer state = addrRuleState(userName, eventName, addTime);
    Integer id = searchUserId(userName);
    UpdateWrapper<AddrRule> queryWrapper = new UpdateWrapper<>();
    queryWrapper.eq("id", userName).eq("event_name", eventName).eq("event_add_time", addTime);
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
      List<AddrRule> listSave = addrRuleList(list, id);
      return this.updateBatchById(listSave);
    } else {
      throw new DataBaseException(State.USER_NOT_EXIST);
    }
  }
}
