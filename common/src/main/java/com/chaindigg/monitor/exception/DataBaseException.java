package com.chaindigg.monitor.exception;

public class DataBaseException extends Exception {
  private String message;

  public DataBaseException() {}

  public DataBaseException(String message) {
    super(message);
    this.message = message;
  }
}
