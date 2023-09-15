package me.coonect.coonect.member.adapter.in.web;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.coonect.coonect.member.application.port.in.NicknameValidationQuery;
import me.coonect.coonect.member.application.port.in.NicknameValidationUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class NicknameValidationController {

  private final NicknameValidationUseCase nicknameValidationUseCase;

  @GetMapping("/member/nickname/valid")
  public ResponseEntity<Void> validNickname(@RequestParam String nickname) {
    if (nicknameValidationUseCase.isNicknameDuplicated(NicknameValidationQuery.of(nickname))) {
      return ResponseEntity.status(HttpServletResponse.SC_CONFLICT).build();
    }

    return ResponseEntity.ok().build();
  }
}
