package com.chaindigg.monitor.utils;

import com.chaindigg.monitor.enums.StatusCode;
import lombok.Data;

@Data
public class ApiResponse<T> {

  private Integer code;
  private String msg;
  private T data;

  public ApiResponse<T> success(T t) {
    this.code = StatusCode.SUCCESS.code;
    this.msg = StatusCode.SUCCESS.message;
    this.data = t;
    return this;
  }

  public ApiResponse<T> fail() {
    this.code = StatusCode.FAIL.code;
    this.msg = StatusCode.FAIL.message;
    this.data = null;
    return this;
  }

}
