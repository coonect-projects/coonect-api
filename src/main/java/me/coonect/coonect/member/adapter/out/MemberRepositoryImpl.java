package me.coonect.coonect.member.adapter.out;

import lombok.RequiredArgsConstructor;
import me.coonect.coonect.member.application.domain.model.Member;
import me.coonect.coonect.member.application.port.out.MemberRepository;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MemberRepositoryImpl implements MemberRepository {

  private final MemberJpaRepository memberJpaRepository;

  @Override
  public Member save(Member member) {
    return memberJpaRepository.save(MemberJpaEntity.from(member)).toMember();
  }

  @Override
  public boolean existsByNickname(String nickname) {
    return memberJpaRepository.existsByNickname(nickname);
  }

  @Override
  public boolean existsByEmail(String email) {
    return memberJpaRepository.existsByEmail(email);
  }


}
