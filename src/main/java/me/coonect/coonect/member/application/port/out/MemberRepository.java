package me.coonect.coonect.member.application.port.out;

import me.coonect.coonect.member.application.domain.model.Member;

public interface MemberRepository {

  Member save(Member member);

  boolean existsByNickname(String nickname);

  boolean existsByEmail(String email);

}
