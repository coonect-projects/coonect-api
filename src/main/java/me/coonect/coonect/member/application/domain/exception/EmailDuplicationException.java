package me.coonect.coonect.member.application.domain.exception;

import me.coonect.coonect.common.error.exception.DuplicationException;

public class EmailDuplicationException extends DuplicationException {

  public EmailDuplicationException(final String email) {
    super(email);
  }
}
