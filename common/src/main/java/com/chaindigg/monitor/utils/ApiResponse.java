package com.chaindigg.monitor.utils;

import com.chaindigg.monitor.enums.State;
import lombok.Data;

@Data
public class ApiResponse {

  private boolean isok;
  private Integer code;
  private String msg;
  private Object data;

  //  public ApiResponse<T> success(T t) {
  //    this.code = State.SUCCESS.code;
  //    this.msg = State.SUCCESS.message;
  //    this.data = t;
  //    return this;
  //  }
  //
  //  public ApiResponse<T> fail() {
  //    this.code = State.FAIL.code;
  //    this.msg = State.FAIL.message;
  //    this.data = (T) ResponseMSG.OPERATE_FAIL.message;
  //    return this;
  //  }

  private static ApiResponse success(Object t, State s) {
    ApiResponse apiResponse = new ApiResponse();
    apiResponse.isok = true;
    apiResponse.code = s.code;
    apiResponse.msg = s.message;
    apiResponse.data = t;
    return apiResponse;
  }

  private static ApiResponse fail(State s) {
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
        if (t.equals(true)) {
          return success(t[0], s);
        } else {
          return fail(s);
        }
      }
      return success(t[0], s);
    } else {
      return fail(s);
    }
  }

  //  public ApiResponse<T> response(T t) {
  //    if (t instanceof Boolean) {
  //      if (t.equals(true)) {
  //        return success(t);
  //      } else {
  //        return fail();
  //      }
  //    } else if (t instanceof Collection) {
  //      List list = new ArrayList();
  //      Boolean flag = t.toString() == list.toString();
  //      if (!flag) {
  //        return success(t);
  //      } else {
  //        return fail();
  //      }
  //    } else if (t instanceof Map) {
  //      Map map = new HashMap();
  //      Boolean flag = t.toString() == map.toString();
  //      if (!flag) {
  //        return success(t);
  //      } else {
  //        return fail();
  //      }
  //    } else {
  //      return fail();
  //    }
  //  }
}
