package me.coonect.coonect.member.application.domain.exception;

import me.coonect.coonect.common.error.exception.DuplicationException;

public class NicknameDuplicationException extends DuplicationException {

  public NicknameDuplicationException(final String email) {
    super(email);
  }
}
