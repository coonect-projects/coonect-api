package me.coonect.coonect.member.adapter.in;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.coonect.coonect.member.application.domain.model.Member;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberResponse {

  private Long id;
  private String email;
  private String nickname;

  public static MemberResponse from(Member member) {
    return new MemberResponse(
        member.getId(),
        member.getEmail(),
        member.getNickname());
  }
}
