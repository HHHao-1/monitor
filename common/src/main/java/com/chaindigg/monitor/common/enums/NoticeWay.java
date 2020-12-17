package com.chaindigg.monitor.common.enums;

public enum NoticeWay {
  // 通知方式
  SMS(0, "短信提醒"),
  EMAIL(1, "邮件提醒"),
  CLIENT(2, "客户端提醒"),
  SMS_AND_EMAIL(3, "短信+邮件提醒"),
  SMS_AND_CLIENT(4, "短信+客户端提醒"),
  EMAIL_AND_CLIENT(5, "邮件+客户端提醒"),
  SMS_EMAIL_CLIENT(6, "短信+邮件+客户端提醒"),
  Exception_WAY(001, "无效的提醒方式");
  
  NoticeWay(Integer code, String message) {
    this.code = code;
    this.message = message;
  }
  
  public int code;
  
  public String message;
}
