package me.coonect.coonect.member.application.domain.service;

import lombok.RequiredArgsConstructor;
import me.coonect.coonect.member.application.domain.exception.EmailDuplicationException;
import me.coonect.coonect.member.application.domain.exception.NicknameDuplicationException;
import me.coonect.coonect.member.application.domain.model.Member;
import me.coonect.coonect.member.application.port.in.MemberSignupCommand;
import me.coonect.coonect.member.application.port.in.MemberSignupUseCase;
import me.coonect.coonect.member.application.port.out.persistence.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberSignupService implements MemberSignupUseCase {

  private final MemberRepository memberRepository;

  @Transactional
  @Override
  public Member signup(final MemberSignupCommand command) {
    if (memberRepository.existsByEmail(command.getEmail())) {
      throw new EmailDuplicationException(command.getEmail());
    }

    if (memberRepository.existsByNickname(command.getNickname())) {
      throw new NicknameDuplicationException(command.getNickname());
    }

    // TODO: VerifiedEmailRepository 에 코드 및 이메일 존재 확인 후 가입 진행

    return memberRepository.save(command.toMember());
  }
}
