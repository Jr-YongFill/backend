package com.yongfill.server.domain.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AuthProvider implements AuthenticationProvider {

    private final BCryptPasswordEncoder passwordEncoder;
    private final CustomMemberDetailsService memberDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = (String) authentication.getPrincipal();   // 사용자의 이메일
        String password = (String) authentication.getCredentials(); // 사용자의 비밀번호

        // 이메일로 사용자 정보 가져온다.
        UserDetails userDetails = memberDetailsService.loadUserByEmail(email);

        // 가져온 사용자 정보와 입력된 비밀번호를 비교하여 인증을 수행합니다.
        if(userDetails != null && passwordEncoder.matches(password, userDetails.getPassword())) {
            // 인증 성공 시 UsernamePasswordAuthenticationToken을 반환합니다.
            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        } else {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
