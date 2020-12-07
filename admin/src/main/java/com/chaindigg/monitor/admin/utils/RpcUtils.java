package com.chaindigg.monitor.admin.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhifantech.util.BitcoindPoolUtil;
import com.zhifantech.util.ParityPoolUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@PropertySource(value = {"classpath:rpc.properties"})
public class RpcUtils {
  
  @Value("${retry-num}")
  private int rpcRetryNum; // 链接失败重试次数
  @Value("${retry-interv}")
  private int rpcRetryInterv; // 重新链接间隔时间
  @Value("${user-name}")
  private String username; // 用户名
  @Value("${password}")
  private String password; // 密码
  
  public void bitInit(List<String> urlList) {
    try {
      BitcoindPoolUtil.init(urlList, username, password, rpcRetryNum, rpcRetryInterv);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public void ethInit(List<String> urlList) {
    try {
      ParityPoolUtil.init(urlList, rpcRetryNum, rpcRetryInterv);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  
  public QueryWrapper[] createQueryConditions(String coinKind) {
    QueryWrapper addrQueryWrapper = new QueryWrapper();
    addrQueryWrapper.select("id", "address").eq("state", 1);
    addrQueryWrapper.eq("coin_kind", "BCH");
    QueryWrapper transQueryWrapper = new QueryWrapper();
    transQueryWrapper.select("id", "monitor_min_val").eq("state", 1);
    transQueryWrapper.eq("coin_kind", "BCH");
    return new QueryWrapper[]{addrQueryWrapper, transQueryWrapper};
  }
}
