package me.coonect.coonect.member.application.domain.service;

import lombok.RequiredArgsConstructor;
import me.coonect.coonect.member.application.port.in.model.NicknameValidationQuery;
import me.coonect.coonect.member.application.port.in.NicknameValidationUseCase;
import me.coonect.coonect.member.application.port.out.persistence.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class NicknameValidationService implements NicknameValidationUseCase {

  private final MemberRepository memberRepository;

  @Override
  public boolean isNicknameDuplicated(NicknameValidationQuery query) {
    return memberRepository.existsByNickname(query.getNickname());
  }
}
