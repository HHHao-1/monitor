package com.chaindigg.monitor.admin.utils;

import com.zhifantech.util.BitcoindPoolUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

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
  
  public static void init(List<String> urlList) {
    try {
      BitcoindPoolUtil.init(urlList, username, password, rpcRetryNum, rpcRetryInterv);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
