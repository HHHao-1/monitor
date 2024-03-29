package com.chaindigg.monitor.common.utils;

import com.chaindigg.monitor.common.enums.State;
import lombok.Data;

import java.util.List;

@Data
public class ApiResponse {

  private boolean isok;
  private Integer code;
  private String msg;
  private Object data;

  public static ApiResponse success(Object t, State s) {
    ApiResponse apiResponse = new ApiResponse();
    apiResponse.isok = true;
    apiResponse.code = s.code;
    apiResponse.msg = s.message;
    apiResponse.data = t;
    return apiResponse;
  }

  public static ApiResponse fail(State s) {
    ApiResponse apiResponse = new ApiResponse();
    apiResponse.isok = false;
    apiResponse.code = s.code;
    apiResponse.msg = s.message;
    apiResponse.data = null;
    return apiResponse;
  }

  public static ApiResponse create(State s, Object... t) {
    if (t.length != 0) {
      if (t[0] instanceof Boolean) {
        if (new Boolean(t[0].toString()) == true) {
          return success(t[0], s);
        } else {
          return fail(State.FAIL);
        }
      } else if (t[0] instanceof List) {
        if (((List) t[0]).size() != 0) {
          return success(t[0], s);
        } else {
          return fail(State.FAIL);
        }
      } else {
        return success(t[0], s);
      }
    } else {
      return fail(s);
    }
  }
}
