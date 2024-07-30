package com.yongfill.server.global.filter;

import com.yongfill.server.domain.auth.service.CustomMemberDetailsService;
import com.yongfill.server.global.config.JwtTokenProvider;
import com.yongfill.server.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.yongfill.server.global.common.response.error.ErrorCode.NOT_TOKEN;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomMemberDetailsService customMemberDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String accessToken = getTokenFromRequest(request);

            if (accessToken == null) {
                throw new CustomException(NOT_TOKEN);
            }

            if (jwtTokenProvider.validateToken(accessToken)) {
                UsernamePasswordAuthenticationToken authentication = getAuthenticationFromToken(accessToken);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            request.setAttribute("exception", e);
        }

        filterChain.doFilter(request, response);
    }

    // JWT 토큰 추출
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 이후 토큰 값만 반환
        }
        return null;
    }

    // JWT 토큰으로부터 인증 객체를 생성합니다.
    private UsernamePasswordAuthenticationToken getAuthenticationFromToken(String accessToken) {
        Long memberId = jwtTokenProvider.getUserIdFromToken(accessToken);
        UserDetails userDetails = customMemberDetailsService.loadUserByMemberId(memberId);

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}