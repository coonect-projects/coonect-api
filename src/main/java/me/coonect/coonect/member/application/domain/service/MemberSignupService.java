package me.coonect.coonect.member.application.domain.service;

import lombok.RequiredArgsConstructor;
import me.coonect.coonect.common.error.exception.ErrorCode;
import me.coonect.coonect.common.error.exception.NotFoundException;
import me.coonect.coonect.member.application.domain.exception.EmailDuplicationException;
import me.coonect.coonect.member.application.domain.exception.NicknameDuplicationException;
import me.coonect.coonect.member.application.domain.model.Member;
import me.coonect.coonect.member.application.port.in.MemberSignupUseCase;
import me.coonect.coonect.member.application.port.in.model.MemberSignupCommand;
import me.coonect.coonect.member.application.port.out.persistence.MemberRepository;
import me.coonect.coonect.member.application.port.out.persistence.VerifiedEmailRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberSignupService implements MemberSignupUseCase {

  private final MemberRepository memberRepository;
  private final VerifiedEmailRepository verifiedEmailRepository;

  @Transactional
  @Override
  public Member signup(final MemberSignupCommand command) {
    String email = command.getEmail();
    String nickname = command.getNickname();
    String code = command.getEmailVerificationCode();

    if (memberRepository.existsByEmail(email)) {
      throw new EmailDuplicationException(email);
    }

    if (memberRepository.existsByNickname(nickname)) {
      throw new NicknameDuplicationException(nickname);
    }

    if (!verifyCode(email, code)) {
      throw new NotFoundException(ErrorCode.VERIFIED_EMAIL_NOT_FOUND);
    }

    return memberRepository.save(command.toMember());
  }

  private boolean verifyCode(String email, String code) {
    return verifiedEmailRepository.has(code) && verifiedEmailRepository.get(code).equals(email);
  }
}
