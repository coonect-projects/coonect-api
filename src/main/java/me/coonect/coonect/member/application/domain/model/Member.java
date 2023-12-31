package me.coonect.coonect.member.application.domain.model;

import java.time.LocalDate;
import lombok.Getter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
public class Member {

  private static final PasswordEncoder passwordEncoder =
      PasswordEncoderFactories.createDelegatingPasswordEncoder();
  private final String email;
  private Long id;
  private String encodedPassword;
  private String nickname;
  private LocalDate birthday;

  public Member(Long id, String email, String encodedPassword, String nickname,
      LocalDate birthday) {
    this.id = id;
    this.email = email;
    this.encodedPassword = encodedPassword;
    this.nickname = nickname;
    this.birthday = birthday;
  }

  public static Member withoutId(String email, String rawPassword, String nickname,
      LocalDate birthday) {
    String encodedPassword = passwordEncoder.encode(rawPassword);
    return new Member(null, email, encodedPassword, nickname, birthday);
  }

  public static Member withEncodedPassword(Long id, String email, String encodedPassword,
      String nickname,
      LocalDate birthday) {
    return new Member(id, email, encodedPassword, nickname, birthday);
  }
}
