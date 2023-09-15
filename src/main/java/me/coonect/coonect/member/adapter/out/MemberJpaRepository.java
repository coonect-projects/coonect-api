package me.coonect.coonect.member.adapter.out;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<MemberJpaEntity, Long> {

  boolean existsByNickname(String nickname);

  boolean existsByEmail(String email);
}
