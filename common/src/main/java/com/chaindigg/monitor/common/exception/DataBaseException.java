package com.chaindigg.monitor.common.exception;

import com.chaindigg.monitor.common.enums.State;

public class DataBaseException extends Exception {
  public State state;

  public DataBaseException() {}

  public DataBaseException(State state) {
    super(state.message);
    this.state = state;
  }
}
