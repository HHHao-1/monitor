package com.chaindigg.monitor.enums;

public enum StatusCode {
  //状态码
  SUCCESS(1, "请求成功"),
  FAIL(0, "请求失败");

  StatusCode(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public int code;

  public String message;
}
