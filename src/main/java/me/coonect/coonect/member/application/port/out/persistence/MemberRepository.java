package me.coonect.coonect.member.application.port.out.persistence;

import java.util.Optional;
import me.coonect.coonect.member.application.domain.model.Member;

public interface MemberRepository {

  Member save(Member member);

  boolean existsByNickname(String nickname);

  boolean existsByEmail(String email);

  Optional<Member> findByEmail(String email);
}
