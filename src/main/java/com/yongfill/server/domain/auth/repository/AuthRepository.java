package com.yongfill.server.domain.auth.repository;

import com.yongfill.server.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Member, Long> {
    // 리프레시 토큰으로 회원 찾기
    Optional<Member> findByRefreshToken(String refreshToken);

    // 이메일로 회원 존재 여부 확인
    boolean existsByEmail(String email);

}