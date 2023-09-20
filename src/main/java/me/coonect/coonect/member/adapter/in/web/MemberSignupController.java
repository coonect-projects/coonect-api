package me.coonect.coonect.member.adapter.in.web;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.coonect.coonect.member.adapter.in.web.dto.request.MemberSignupRequest;
import me.coonect.coonect.member.adapter.in.web.dto.response.MemberResponse;
import me.coonect.coonect.member.application.domain.model.Member;
import me.coonect.coonect.member.application.port.in.MemberSignupUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MemberSignupController {

  private final MemberSignupUseCase memberSignupUseCase;

  @PostMapping("/member")
  public ResponseEntity<MemberResponse> signup(@RequestBody MemberSignupRequest request) {
    Member member = memberSignupUseCase.signup(request.toCommand());
    return ResponseEntity.status(HttpServletResponse.SC_CREATED).body(MemberResponse.from(member));
  }
}