package com.bitalino;

public class BITalinoException extends java.lang.Exception {

  private static final long serialVersionUID = 1L;

  private final int code;

  public BITalinoException(final BITalinoErrorTypes errorType) {
    super(errorType.getDescription());
    code = errorType.getValue();
  }

  public int getCode() {
    return code;
  }

}