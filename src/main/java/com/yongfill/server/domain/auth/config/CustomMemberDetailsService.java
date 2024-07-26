package com.yongfill.server.domain.auth.config;

import com.yongfill.server.domain.member.entity.Member;
import com.yongfill.server.domain.member.repository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomMemberDetailsService implements UserDetailsService {
    private final MemberJpaRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String nickName) throws UsernameNotFoundException {
        Member member = memberRepository.findMemberByNickname(nickName).orElseThrow(
                () -> new UsernameNotFoundException(""));

        return new CustomMemberDetails(member);
    }

    // 필요시 추가
    public UserDetails loadUserByMemberId(Long id) throws IllegalArgumentException {
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException(""));

        return new CustomMemberDetails(member);
    }

    // 필요시 추가
    public UserDetails loadUserByEmail(String email) throws IllegalArgumentException {
        // 이메일을 기반으로 회원 정보를 데이터베이스에서 가져옵니다.
        Member member = memberRepository.findMemberByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("해당 이메일을 가진 사용자를 찾을 수 없습니다.")
        );

        return new CustomMemberDetails(member);
    }
}