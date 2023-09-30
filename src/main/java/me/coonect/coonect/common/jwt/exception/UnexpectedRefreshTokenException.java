package me.coonect.coonect.common.jwt.exception;

import com.auth0.jwt.exceptions.JWTVerificationException;

public class UnexpectedRefreshTokenException extends JWTVerificationException {

  public UnexpectedRefreshTokenException() {
    super("Connect From Another Location");
  }
}

