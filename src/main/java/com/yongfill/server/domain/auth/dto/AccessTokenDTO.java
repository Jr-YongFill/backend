package com.yongfill.server.domain.auth.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AccessTokenDTO {
    private String tokenType;
    private String accessToken; // 액세스 토큰
    private String refreshToken;


    @Builder
    public AccessTokenDTO(String accessToken, String refreshToken, String tokenType) {
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}