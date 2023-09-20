package me.coonect.coonect.common.error.exception;

public class NotFoundException extends BusinessException {

  public NotFoundException() {
    super(ErrorCode.NOT_FOUND.getMessage(), ErrorCode.NOT_FOUND);
  }

  public NotFoundException(ErrorCode errorCode) {
    super(errorCode.getMessage(), errorCode);
  }
}
