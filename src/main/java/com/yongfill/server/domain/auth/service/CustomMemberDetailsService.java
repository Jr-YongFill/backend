package com.yongfill.server.domain.auth.service;

import com.yongfill.server.domain.member.entity.Member;
import com.yongfill.server.domain.member.repository.MemberJpaRepository;
import com.yongfill.server.global.common.response.error.ErrorCode;
import com.yongfill.server.global.exception.CustomException;
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
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        Member member = memberRepository.findMemberByNickname(nickname).orElseThrow(
                () -> new UsernameNotFoundException("nickname " + nickname + "을 찾을 수 없다"));
        return new CustomMemberDetails(member);
    }

    public UserDetails loadUserByMemberId(Long memberId) throws IllegalArgumentException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("id를 찾을 수 없습니다"));

        return new CustomMemberDetails(member);
    }

    public UserDetails loadUserByEmail(String email) throws IllegalArgumentException {
        Member member = memberRepository.findMemberByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("Email를 찾을 수 없습니다")
        );

        return new CustomMemberDetails(member);
    }
}