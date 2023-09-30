package me.coonect.coonect.common.jwt.application.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResponse {

  private String tokenType;
  private String accessToken;
  private Long accessTokenExpiresIn;
  private String refreshToken;
  private Long refreshTokenExpiresIn;

}
