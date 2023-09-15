package me.coonect.coonect.member.application.domain.service;

import lombok.RequiredArgsConstructor;
import me.coonect.coonect.member.application.domain.exception.EmailDuplicationException;
import me.coonect.coonect.member.application.domain.exception.NicknameDuplicationException;
import me.coonect.coonect.member.application.domain.model.Member;
import me.coonect.coonect.member.application.domain.model.MemberSignup;
import me.coonect.coonect.member.application.port.in.MemberSignupUseCase;
import me.coonect.coonect.member.application.port.out.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberSignupService implements MemberSignupUseCase {

  private final MemberRepository memberRepository;

  @Transactional
  @Override
  public Member signup(final MemberSignup memberSignup) {
    if (memberRepository.existsByEmail(memberSignup.getEmail())) {
      throw new EmailDuplicationException(memberSignup.getEmail());
    }

    if (memberRepository.existsByNickname(memberSignup.getNickname())) {
      throw new NicknameDuplicationException(memberSignup.getNickname());
    }

    return memberRepository.save(memberSignup.toMember());
  }
}
