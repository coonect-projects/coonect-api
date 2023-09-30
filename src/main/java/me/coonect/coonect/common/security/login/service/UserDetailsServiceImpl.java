package me.coonect.coonect.common.security.login.service;

import lombok.RequiredArgsConstructor;
import me.coonect.coonect.member.application.domain.model.Member;
import me.coonect.coonect.member.application.port.out.persistence.MemberRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Member member = memberRepository.findByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자 입니다."));
    return User.builder()
        .username(member.getEmail())
        .password(member.getEncodedPassword())
        .roles("MEMBER")
        .build();
  }
}
