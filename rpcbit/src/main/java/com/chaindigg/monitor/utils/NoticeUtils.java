package com.chaindigg.monitor.utils;


import com.zhifantech.base.mail.service.MailService;
import com.zhifantech.base.mail.service.MailServiceImpl;
import com.zhifantech.base.sms.service.SmsService;
import com.zhifantech.base.sms.service.SmsServiceImpl;

import java.util.ArrayList;

public class NoticeUtils {
  
  public static void notice(Integer noticeWay, String userPhone, String userMail, String monitorKind,
                            String smsTemplateCode,
                            ArrayList<String> smsParms, String formatMail) {
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
      //TODO: 微服务通知
      case 2:
        //TODO
        break;
      case 3:
        smsService.sendSms(null, userPhone, smsTemplateCode, smsParms);
        mailService.sendHtmlMail(userMail, "Chaindigg" + monitorKind + "监控提醒", formatMail);
        break;
      case 4:
        smsService.sendSms(null, userPhone, smsTemplateCode, smsParms);
        // 微服务
        break;
      case 5:
        mailService.sendHtmlMail(userMail, "Chaindigg" + monitorKind + "监控提醒", formatMail);
        // 微服务
        break;
      case 6:
        smsService.sendSms(null, userPhone, smsTemplateCode, smsParms);
        mailService.sendHtmlMail(userMail, "Chaindigg" + monitorKind + "监控提醒", formatMail);
        // 微服务
        break;
      default:
        break;
    }
  }
}
