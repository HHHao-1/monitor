package com.chaindigg.monitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaindigg.monitor.entity.TransRule;
import com.chaindigg.monitor.vo.TransRuleVO;
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
public interface ITransRuleVOService extends IService<TransRuleVO> {

  List<TransRuleVO> selectAll(@Nullable String coin, @Nullable String userName, @Nullable String userId, int currentPage, int pageSize);
}
