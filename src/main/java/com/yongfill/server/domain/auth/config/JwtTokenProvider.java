package com.yongfill.server.domain.auth.config;

import java.security.Key;
import java.util.Date;

import com.yongfill.server.domain.auth.service.CustomMemberDetails;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey; // 비밀 키

    @Value("${jwt.expiration.access}")
    private long accessTokenValidityMs; // 액세스 토큰 유효 기간

    @Value("${jwt.expiration.refresh}")
    private long refreshTokenValidityMs; // 리프레시 토큰 유효 기간


    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
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
        Date expiryDate = new Date(new Date().getTime() + refreshTokenValidityMs);

        return Jwts.builder()
                .setSubject(customUserDetails.getUsername())
                .claim("member-id", customUserDetails.getId())
                .claim("member-email", customUserDetails.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(getSigningKey())
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
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        System.out.println("getUserEmailFromToken: " + claims.getSubject());

        return claims.getSubject();
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
        } catch (MalformedJwtException ex) {
            System.out.println("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            System.out.println("Expired JWT token" + ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            System.out.println("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            System.out.println("JWT claims string is empty.");
        }
        return false;
    }
}