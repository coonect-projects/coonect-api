package me.coonect.coonect.member.application.port.in;

import me.coonect.coonect.member.application.domain.model.Member;
import me.coonect.coonect.member.application.domain.model.MemberSignup;

public interface MemberSignupUseCase {

  Member signup(final MemberSignup memberSignup);

}
