package me.coonect.coonect.member.application.port.in;

import me.coonect.coonect.member.application.domain.model.Member;

public interface MemberSignupUseCase {

  Member signup(final MemberSignupCommand memberSignupCommand);

}
