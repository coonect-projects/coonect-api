package me.coonect.coonect.common.error.exception;

import me.coonect.coonect.common.error.ErrorCode;

public class DuplicationException extends BusinessException {

  private String duplicatedValue;

  public DuplicationException(String duplicatedValue) {
    this(duplicatedValue, ErrorCode.DUPLICATE);
    this.duplicatedValue = duplicatedValue;
  }

  public DuplicationException(String value, ErrorCode errorCode) {
    super(value, errorCode);
    this.duplicatedValue = value;
  }
}
