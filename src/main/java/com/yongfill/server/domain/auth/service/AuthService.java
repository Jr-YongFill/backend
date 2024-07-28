package com.yongfill.server.domain.auth.service;

import com.yongfill.server.domain.auth.config.CustomMemberDetails;
import com.yongfill.server.domain.auth.config.CustomMemberDetailsService;
import com.yongfill.server.domain.auth.config.JwtTokenProvider;
import com.yongfill.server.domain.auth.dto.AuthRequestDto;
import com.yongfill.server.domain.auth.dto.AccessTokenDto;
import com.yongfill.server.domain.auth.repository.AuthRepository;
import com.yongfill.server.domain.member.entity.Member;
import com.yongfill.server.domain.member.repository.MemberJpaRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final MemberJpaRepository memberJpaRepository;
    private final CustomMemberDetailsService customMemberDetailsService;

    String tokenType = "Bearer";

    @Transactional
    public AccessTokenDto login(AuthRequestDto requestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.getEmail(),requestDto.getPassword()));

        System.out.println("AccessTokenDTO");
        Member member = ((CustomMemberDetails) authentication.getPrincipal()).getMember();
        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken();

        member.setRefreshToken(refreshToken);
        memberJpaRepository.save(member);

        return new AccessTokenDto(accessToken, refreshToken, tokenType);
    }

    @Transactional
    public AccessTokenDto refreshToken(String refreshToken) {
        // 리프레시 토큰 검증
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        // 리프레시 토큰으로부터 사용자 정보 추출
        String email = jwtTokenProvider.getUserEmailFromToken(refreshToken);
        UserDetails userDetails = customMemberDetailsService.loadUserByUsername(email);

        // 새로운 액세스 토큰 생성
        String newAccessToken = jwtTokenProvider.generateAccessToken(
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
        );
        // 새로운 액세스 토큰을 클라이언트에 반환
        return new AccessTokenDto(newAccessToken, refreshToken, tokenType);
    }
}