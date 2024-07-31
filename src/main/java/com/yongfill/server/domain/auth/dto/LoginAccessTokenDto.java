package com.yongfill.server.domain.auth.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class LoginAccessTokenDto {
    private Long id;
    private String tokenType;
    private String accessToken;
    private String refreshToken;

    @Builder
    public LoginAccessTokenDto(Long memberId, String accessToken, String refreshToken, String tokenType) {
        this.id = memberId;
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
