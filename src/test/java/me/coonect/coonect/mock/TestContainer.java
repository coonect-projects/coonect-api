package me.coonect.coonect.mock;

import lombok.Builder;
import me.coonect.coonect.member.adapter.in.MemberSignupController;
import me.coonect.coonect.member.application.domain.service.MemberSignupService;
import me.coonect.coonect.member.application.port.in.MemberSignupUseCase;
import me.coonect.coonect.member.application.port.out.MemberRepository;

public class TestContainer {

  public MemberRepository memberRepository;

  public MemberSignupUseCase memberSignupUseCase;

  public MemberSignupController memberSignupController;

  @Builder
  public TestContainer() {
    memberRepository = new FakeMemberRepository();

    memberSignupUseCase = new MemberSignupService(memberRepository);

    memberSignupController = new MemberSignupController(memberSignupUseCase);
  }
}
