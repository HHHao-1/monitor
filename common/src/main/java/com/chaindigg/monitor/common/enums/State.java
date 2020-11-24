package com.chaindigg.monitor.common.enums;

public enum State {
  // 状态码
  FAIL(0, "请求失败"),
  SUCCESS(1, "请求成功"),
  USER_NOT_EXIST(2, "用户不存在");

  State(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public int code;

  public String message;
}
