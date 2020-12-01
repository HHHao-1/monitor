package com.chaindigg.monitor.common.api;

import com.zhifantech.api.Rpc;
import com.zhifantech.api.impl.BtcRpc;
import com.zhifantech.strategy.SeqRetryStrategy;
import com.zhifantech.util.BitcoindPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Component
@PropertySource(value = {"classpath:rpc.properties"})
public class BlockRpcInit implements ApplicationRunner {

    @Value("#{'${rpc-urls}'.split(',')}")
    private List<String> rpcUrls; // 节点url

    @Value("${rpc-retry-num}")
    private int rpcRetryNum; // 链接失败重试次数

    @Value("${rpc-retry-interv}")
    private int rpcRetryInterv; // 重新链接间隔时间

    @Value("${rpc-user-name}")
    private String username; // 用户名

    @Value("${rpc-password}")
    private String password; // 密码

    @Resource
    private IBlockRpcInitTools blockRpcInitTools;

    private SeqRetryStrategy seqRetryStrategy;

    @Override
    public void run(ApplicationArguments args) {
        BitcoindPoolUtil.init(rpcUrls, username, password, rpcRetryNum, rpcRetryInterv);
        blockRpcInitTools.monitor();
    }
}

    public List<Rpc> init(List<String> urlList, String username, String password, int retryNum, int sleepInterv) {
        ArrayList rpcList = new ArrayList();

        try {
            Iterator var6 = urlList.iterator();

            while (var6.hasNext()) {
                String url = (String) var6.next();
                Rpc rpc = new BtcRpc();
                boolean status = rpc.init(url, username, password);
                if (status) {
                    rpcList.add(rpc);
                }
            }

            seqRetryStrategy = new SeqRetryStrategy(rpcList, retryNum, sleepInterv);
            return rpcList;
        } catch (Exception var10) {
            var10.printStackTrace();
            return null;
        }
    }


