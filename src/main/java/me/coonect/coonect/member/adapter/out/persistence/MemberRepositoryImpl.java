package me.coonect.coonect.member.adapter.out.persistence;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import me.coonect.coonect.member.application.domain.model.Member;
import me.coonect.coonect.member.application.port.out.persistence.MemberRepository;
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

  @Override
  public Optional<Member> findByEmail(String email) {
    return memberJpaRepository.findByEmail(email).map(MemberJpaEntity::toMember);
  }


}
