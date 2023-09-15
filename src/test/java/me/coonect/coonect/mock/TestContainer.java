package me.coonect.coonect.mock;

import lombok.Builder;
import me.coonect.coonect.member.adapter.in.web.MemberSignupController;
import me.coonect.coonect.member.adapter.in.web.NicknameValidationController;
import me.coonect.coonect.member.application.domain.service.MemberSignupService;
import me.coonect.coonect.member.application.domain.service.NicknameValidationService;
import me.coonect.coonect.member.application.port.in.MemberSignupUseCase;
import me.coonect.coonect.member.application.port.in.NicknameValidationUseCase;
import me.coonect.coonect.member.application.port.out.persistence.MemberRepository;

public class TestContainer {

  public MemberRepository memberRepository;

  public MemberSignupUseCase memberSignupUseCase;
  public NicknameValidationUseCase nicknameValidationUseCase;

  public MemberSignupController memberSignupController;
  public NicknameValidationController nicknameValidationController;

  @Builder
  public TestContainer() {
    memberRepository = new FakeMemberRepository();

    memberSignupUseCase = new MemberSignupService(memberRepository);
    nicknameValidationUseCase = new NicknameValidationService(memberRepository);

    memberSignupController = new MemberSignupController(memberSignupUseCase);
    nicknameValidationController = new NicknameValidationController(nicknameValidationUseCase);
  }
}
