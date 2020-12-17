package com.chaindigg.monitor;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.io.IOException;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = MonitorBitApplication.class)
@WebAppConfiguration
class MonitorApplicationTests {
  @Resource
  private DesktopNotifyProvider desktopNotifyProvider;
  
  @Test
  void test() throws IOException {
    desktopNotifyProvider.sendMessage("123456", "1");
  }
  
}
