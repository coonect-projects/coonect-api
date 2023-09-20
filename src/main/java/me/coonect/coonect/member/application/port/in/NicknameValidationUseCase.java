package me.coonect.coonect.member.application.port.in;

import me.coonect.coonect.member.application.port.in.model.NicknameValidationQuery;

public interface NicknameValidationUseCase {

  boolean isNicknameDuplicated(NicknameValidationQuery query);
}
