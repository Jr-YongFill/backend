package com.yongfill.server.global.config;

import java.security.Key;
import java.util.Date;

import com.yongfill.server.domain.auth.service.CustomMemberDetails;
import com.yongfill.server.global.exception.CustomException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import static com.yongfill.server.global.common.response.error.ErrorCode.*;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey; // JWT 서명에 사용할 비밀 키

    @Value("${jwt.expiration.access}")
    private long accessTokenValidityMs; // 액세스 토큰 유효 기간

    @Value("${jwt.expiration.refresh}")
    private long refreshTokenValidityMs; // 리프레시 토큰 유효 기간


    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes); //JWT 토큰 서명을 위한 키
    }

    // 액세스 토큰 생성
    public String generateAccessToken(Authentication authentication) {
        CustomMemberDetails customUserDetails = (CustomMemberDetails) authentication.getPrincipal();
        Date expiryDate = new Date(new Date().getTime() + accessTokenValidityMs);

        return Jwts.builder()
                .setSubject(customUserDetails.getUsername())
                .claim("member-id", customUserDetails.getMember().getId())
                .claim("member-email", customUserDetails.getMember().getEmail())
                .claim("member-nickname", customUserDetails.getMember().getEmail())
                .claim("auth", customUserDetails.getMember().getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Authentication authentication) {
        CustomMemberDetails customUserDetails = (CustomMemberDetails) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(customUserDetails.getUsername())
                .claim("member-id", customUserDetails.getMember().getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidityMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Long getUserIdFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("member-id", Long.class);
    }

    public String getUserEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("member-email", String.class);
    }

    // JWT 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts
                    .parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (SecurityException | MalformedJwtException ex) {
            throw new CustomException(INVALID_JWT_TOKEN);
        } catch (ExpiredJwtException ex) {
            throw new CustomException(EXPIRED_JWT_TOKEN);
        } catch (UnsupportedJwtException ex) {
            throw new CustomException(UNSUPPORTED_JWT_TOKEN);
        } catch (IllegalArgumentException ex) {
            throw new CustomException(NON_LOGIN);
        }
    }
}
