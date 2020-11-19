//package com.chaindigg.monitor.service.impl;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.core.toolkit.StringUtils;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.chaindigg.monitor.dao.AddrRuleMapper;
//import com.chaindigg.monitor.entity.AddrRule;
//import com.chaindigg.monitor.entity.CoinKind;
//import com.chaindigg.monitor.service.IAddrRuleService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.lang.Nullable;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
///**
// * <p>
// * 服务实现类
// * </p>
// *
// * @author chenghao
// * @since 2020-11-17
// */
//@Service
//@RequiredArgsConstructor
//public class AddrRuleServiceImpl extends ServiceImpl<AddrRuleMapper, AddrRule> implements IAddrRuleService {
//  private final AddrRuleMapper addrRuleMapper;
//
////  @Override
////  public List<AddrRule> selectByUserId(String id, int currentPage, int pageSize) {
////    IPage<CoinKind> page = new Page<CoinKind>(currentPage, pageSize);
////    return addrRuleMapper.selectByUserId(page, id);
////  }
//
//  public List<AddrRule> selectAll(@Nullable String event, @Nullable String userName, @Nullable String userId, int currentPage, int pageSize) {
//    IPage<AddrRule> page = new Page<AddrRule>(currentPage, pageSize);
//    QueryWrapper<AddrRule> queryWrapper = new QueryWrapper<>();
//    queryWrapper.orderByDesc("id");
//    if(!StringUtils.isBlank(userId)){
//      queryWrapper.eq("user_id",userId);
//    }else if(!StringUtils.isBlank(userName)&& !StringUtils.isBlank(event)){
//      queryWrapper.eq("event_name",event).eq("user_name",userName);
//    }
//    if (!StringUtils.isBlank(event) && StringUtils.isBlank(userName)){
//      queryWrapper.eq("event_name",event);
//    }else if(StringUtils.isBlank(event) && !StringUtils.isBlank(userName)){
//      queryWrapper.eq("user_name",userName);
//    }
//    return this.page(page,queryWrapper).getRecords();
//  }
//}
