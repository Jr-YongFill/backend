package com.yongfill.server.global.config;

import com.yongfill.server.domain.member.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final MemberService memberService;

    public SecurityConfig(MemberService memberService) {
        this.memberService = memberService;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/").permitAll() // auth 모든 사용자에게 허용
                        .requestMatchers("/api/admin/**").hasRole("ADMIN") // ADMIN 권한이 필요한 요청
                )
//                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
                ;
                return http.build();
    }

}
