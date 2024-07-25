package com.spring.security.auth.dto;

import com.spring.security.auth.domain.entity.Auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuthResponseDto {
    private String tokenType;
    private String accessToken;
    private String refreshToken;
    
    @Builder
    public AuthResponseDto(Auth entity) {
    	this.tokenType = entity.getTokenType();
        this.accessToken = entity.getAccessToken();
        this.refreshToken = entity.getRefreshToken();
    }
}