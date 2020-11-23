package com.chaindigg.monitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaindigg.monitor.vo.AddrRuleVO;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author chenghao
 * @since 2020-11-17
 */
public interface IAddrRuleVOService extends IService<AddrRuleVO> {

  List<AddrRuleVO> selectAll(@Nullable String event, @Nullable String userName, @Nullable String userId, int currentPage, int pageSize);

}
