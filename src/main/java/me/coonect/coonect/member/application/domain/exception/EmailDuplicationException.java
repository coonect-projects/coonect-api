package me.coonect.coonect.member.application.domain.exception;

import me.coonect.coonect.common.error.exception.DuplicationException;
import me.coonect.coonect.common.error.exception.ErrorCode;

public class EmailDuplicationException extends DuplicationException {

  private static final ErrorCode errorCode = ErrorCode.EMAIL_DUPLICATE;

  public EmailDuplicationException(final String email) {
    super(email, errorCode);
  }
}
