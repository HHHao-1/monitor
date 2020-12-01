package com.chaindigg.monitor.common.enums;

public enum State {
  // 状态码
  FAIL(0000, "请求失败"),
  SUCCESS(0001, "请求成功"),
  USER_NOT_EXIST(0002, "用户不存在"),
  INSERT_FAIL(0003, "存入失败");

  State(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public int code;

  public String message;
}
