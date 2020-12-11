package com.chaindigg.monitor.common.enums;

public enum State {
  // 状态码
  FAIL(1000, "请求失败"),
  SUCCESS(1001, "请求成功"),
  USER_NOT_EXIST(1002, "用户不存在"),
  INSERT_FAIL(1003, "存入失败");
  
  State(Integer code, String message) {
    this.code = code;
    this.message = message;
  }
  
  public Integer code;
  
  public String message;
}
