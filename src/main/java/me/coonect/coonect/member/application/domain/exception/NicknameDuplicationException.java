package me.coonect.coonect.member.application.domain.exception;

import me.coonect.coonect.common.error.exception.DuplicationException;
import me.coonect.coonect.common.error.exception.ErrorCode;

public class NicknameDuplicationException extends DuplicationException {

  private static final ErrorCode errorCode = ErrorCode.NICKNAME_DUPLICATE;

  public NicknameDuplicationException(final String nickname) {
    super(nickname, errorCode);
  }
}
