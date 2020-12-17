package com.chaindigg.monitor.utils;


import com.chaindigg.monitor.common.utils.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

//@Component
@FeignClient("desktop-notify-provider")
public interface DesktopNotifyProvider {
  
  @GetMapping(value = "/send")
  ApiResponse sendMessage(@RequestParam("msg") String message,
                          @Nullable @RequestParam("id") String id) throws IOException;
  
}
