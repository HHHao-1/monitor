package com.chaindigg.monitor.admin;

import com.zhifantech.util.BitcoindPoolUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class RpcTests {

  @Test
  void contextLoads() throws Exception {
    List<String> rpcUrls = new ArrayList<>();
    rpcUrls.add("http://10.0.0.17:8332/ "); // 节点地址
    int rpcRetryNum = 6; // 链接失败重试次数
    int rpcRetryInterv = 100; // 重新链接间隔时间
    String username = "hduser"; // 用户名
    String password = "N8aXG9VgezhkBnF2iAMpeG"; // 密码
    BitcoindPoolUtil.init(rpcUrls, username, password, rpcRetryNum, rpcRetryInterv);
    Long maxBlockHeight = BitcoindPoolUtil.getMaxBlockHeight();
    System.out.println(maxBlockHeight);
  }
}
