package com.chaindigg.monitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaindigg.monitor.vo.TransRuleVO;

import java.util.List;

/**
 * 服务类
 *
 * @author chenghao
 * @since 2020-11-17
 */
public interface ITransRuleVOService extends IService<TransRuleVO> {

  List<TransRuleVO> selectAll(
      String coin, String userName, String userId, int currentPage, int pageSize);
}
