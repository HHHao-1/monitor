package com.chaindigg.monitor.exception;

import com.chaindigg.monitor.enums.State;

public class DataBaseException extends Exception {
  public State state;

  public DataBaseException() {}

  public DataBaseException(State state) {
    super(state.message);
    this.state = state;
  }
}
