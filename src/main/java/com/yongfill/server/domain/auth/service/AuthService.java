package com.yongfill.server.domain.auth.service;

import com.yongfill.server.domain.auth.config.CustomMemberDetails;
import com.yongfill.server.domain.auth.config.JwtTokenProvider;
import com.yongfill.server.domain.auth.dto.AuthRequestDto;
import com.yongfill.server.domain.auth.dto.AuthResponseDto;
import com.yongfill.server.domain.auth.repository.AuthRepository;
import com.yongfill.server.domain.member.dto.MemberRequestDTO;
import com.yongfill.server.domain.member.entity.Member;
import com.yongfill.server.domain.member.entity.Role;
import com.yongfill.server.domain.member.repository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthRepository authRepository;
    private final AuthenticationManager authenticationManager;
    private final MemberJpaRepository memberJpaRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthResponseDto login(AuthRequestDto requestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.getEmail(),requestDto.getPassword()));

        Member member = ((CustomMemberDetails) authentication.getPrincipal()).getMember();
        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken();

        member.setRefreshToken(refreshToken);
        memberJpaRepository.save(member);

        return new AuthResponseDto(accessToken);
    }
}
