package me.coonect.coonect.member.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.coonect.coonect.member.application.domain.model.Member;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
@Entity
public class MemberJpaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, updatable = false, nullable = false)
  private String email;

  @Column(name = "password", nullable = false)
  private String encodedPassword;

  @Column(unique = true, nullable = false)
  private String nickname;


  private LocalDate birthday;

  MemberJpaEntity(Long id, String email, String encodedPassword, String nickname,
      LocalDate birthday) {
    this.id = id;
    this.email = email;
    this.encodedPassword = encodedPassword;
    this.nickname = nickname;
    this.birthday = birthday;
  }

  static MemberJpaEntity from(Member member) {
    return new MemberJpaEntity(member.getId(),
        member.getEmail(),
        member.getEncodedPassword(),
        member.getNickname(),
        member.getBirthday());
  }

  Member toMember() {
    return Member.withEncodedPassword(id, email, encodedPassword, nickname, birthday);
  }
}
