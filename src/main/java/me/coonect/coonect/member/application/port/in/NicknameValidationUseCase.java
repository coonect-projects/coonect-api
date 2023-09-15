package me.coonect.coonect.member.application.port.in;

public interface NicknameValidationUseCase {

  boolean isNicknameDuplicated(NicknameValidationQuery query);
}
