package com.yongfill.server.domain.auth.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AccessTokenDto {
    private String tokenType;
    private String accessToken; // 액세스 토큰
    private String refreshToken;


    @Builder
    public AccessTokenDto(String accessToken, String refreshToken, String tokenType) {
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
