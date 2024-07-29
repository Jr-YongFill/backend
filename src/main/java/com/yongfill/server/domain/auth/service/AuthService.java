package com.yongfill.server.domain.auth.service;

import com.yongfill.server.domain.auth.config.JwtTokenProvider;
import com.yongfill.server.domain.auth.dto.AuthRequestDTO;
import com.yongfill.server.domain.auth.dto.AccessTokenDTO;
import com.yongfill.server.domain.member.entity.Member;
import com.yongfill.server.domain.member.repository.MemberJpaRepository;
import com.yongfill.server.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.yongfill.server.global.common.response.error.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberJpaRepository memberJpaRepository;
    private final PasswordEncoder passwordEncoder;
    private final String tokenType = "Bearer";

    @Transactional
    public AccessTokenDTO login(AuthRequestDTO requestDto) {
        Member member = memberJpaRepository.findMemberByEmail(requestDto.getEmail()).orElseThrow(
                () -> new CustomException(INVALID_MEMBER));

        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
            throw new CustomException(NOT_MATCH_PASSWORD);
        }

        String accessToken = jwtTokenProvider.generateAccessToken(
                new UsernamePasswordAuthenticationToken(new CustomMemberDetails(member), member.getPassword())
        );
        String refreshToken = jwtTokenProvider.generateRefreshToken(
                new UsernamePasswordAuthenticationToken(new CustomMemberDetails(member), member.getPassword())
        );

        member.setRefreshToken(refreshToken);
        memberJpaRepository.save(member);

        return new AccessTokenDTO(accessToken, refreshToken, tokenType);
    }

    @Transactional
    public AccessTokenDTO refreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new CustomException(EXPIRED_JWT_TOKEN);
        }

        Member member = memberJpaRepository.findMemberByRefreshToken(refreshToken)
                .orElseThrow(() -> new CustomException(INVALID_MEMBER));

        String accessToken = jwtTokenProvider.generateAccessToken(
                new UsernamePasswordAuthenticationToken(new CustomMemberDetails(member), member.getPassword())
        );

        return new AccessTokenDTO(accessToken, refreshToken, tokenType);
    }
}