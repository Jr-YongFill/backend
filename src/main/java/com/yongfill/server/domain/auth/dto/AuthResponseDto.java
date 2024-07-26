package com.yongfill.server.domain.auth.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AuthResponseDto {
    private String accessToken; // 액세스 토큰

    @Builder
    public AuthResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }
}
