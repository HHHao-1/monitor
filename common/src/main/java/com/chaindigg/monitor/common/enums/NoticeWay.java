package com.chaindigg.monitor.common.enums;

public enum NoticeWay {
  // 通知方式
  SMS(0, "短信"),
  EMAIL(1, "邮件"),
  APP(2, "App提醒"),
  CLIENT(3, "客户端提醒");

  NoticeWay(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public int code;

  public String message;
}
