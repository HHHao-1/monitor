package com.chaindigg.monitor.enums;

public enum ResponseMSG {

  OPERATE_SUCCESS("操作成功"),

  OPERATE_FAIL("操作失败"),

  USER_NOT_EXIST("用户不存在");

  ResponseMSG(String message) {
    this.message = message;
  }

  public String message;

}
