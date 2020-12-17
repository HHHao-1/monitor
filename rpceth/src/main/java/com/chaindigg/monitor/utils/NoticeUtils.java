package com.chaindigg.monitor.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chaindigg.monitor.common.dao.UserMapper;
import com.zhifantech.base.mail.service.MailService;
import com.zhifantech.base.mail.service.MailServiceImpl;
import com.zhifantech.base.sms.service.SmsService;
import com.zhifantech.base.sms.service.SmsServiceImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;

@Component
public class NoticeUtils {
  @Resource
  private DesktopNotifyProvider desktopNotifyProvider;
  @Resource
  private UserMapper userMapper;
  
  private String selectUserId(String userPhone) {
    QueryWrapper queryWrapper = new QueryWrapper();
    queryWrapper.select("id").eq("phone", userPhone);
    String userId = String.valueOf(userMapper.selectOne(queryWrapper).getId());
    return userId;
  }
  
  public void notice(Integer noticeWay, String userPhone, String userMail, String monitorKind,
                     String smsTemplateCode,
                     ArrayList<String> smsParms, String formatMail, String coinKind, String unusualCount,
                     String transHash) {
    MailService mailService = new MailServiceImpl();
    SmsService smsService = new SmsServiceImpl();
    mailService.init(null);
    smsService.init(null);
    switch (noticeWay) {
      case 0:
        smsService.sendSms(null, userPhone, smsTemplateCode, smsParms);
        break;
      case 1:
        mailService.sendHtmlMail(userMail, "Chaindigg" + monitorKind + "监控提醒", formatMail);
        break;
      case 2:
        try {
          String msg = "【" + coinKind + "】" + monitorKind + "监控提醒：" + "交易哈希[" + transHash + "],异动金额[" + unusualCount + "]";
          desktopNotifyProvider.sendMessage(msg, selectUserId(userPhone));
        } catch (IOException e) {
          e.printStackTrace();
        }
        break;
      case 3:
        smsService.sendSms(null, userPhone, smsTemplateCode, smsParms);
        mailService.sendHtmlMail(userMail, "Chaindigg" + monitorKind + "监控提醒", formatMail);
        break;
      case 4:
        smsService.sendSms(null, userPhone, smsTemplateCode, smsParms);
        try {
          String msg = "【" + coinKind + "】" + monitorKind + "监控提醒：" + "交易哈希[" + transHash + "],异动金额[" + unusualCount + "]";
          desktopNotifyProvider.sendMessage(msg, selectUserId(userPhone));
        } catch (IOException e) {
          e.printStackTrace();
        }
        break;
      case 5:
        mailService.sendHtmlMail(userMail, "Chaindigg" + monitorKind + "监控提醒", formatMail);
        try {
          String msg = "【" + coinKind + "】" + monitorKind + "监控提醒：" + "交易哈希[" + transHash + "],异动金额[" + unusualCount + "]";
          desktopNotifyProvider.sendMessage(msg, selectUserId(userPhone));
        } catch (IOException e) {
          e.printStackTrace();
        }
        break;
      case 6:
        smsService.sendSms(null, userPhone, smsTemplateCode, smsParms);
        mailService.sendHtmlMail(userMail, "Chaindigg" + monitorKind + "监控提醒", formatMail);
        try {
          String msg = "【" + coinKind + "】" + monitorKind + "监控提醒：" + "交易哈希[" + transHash + "],异动金额[" + unusualCount + "]";
          desktopNotifyProvider.sendMessage(msg, selectUserId(userPhone));
        } catch (IOException e) {
          e.printStackTrace();
        }
        break;
      default:
        break;
    }
  }
}
