package me.coonect.coonect.common.error.exception;

import lombok.Getter;
import me.coonect.coonect.common.error.ErrorCode;

@Getter
public class BusinessException extends RuntimeException {

  private final ErrorCode errorCode;

  public BusinessException(String message, ErrorCode errorCode) {
    super(message);
    this.errorCode = errorCode;
  }

  @Override
  public synchronized Throwable fillInStackTrace() {
    return this;
  }
}
