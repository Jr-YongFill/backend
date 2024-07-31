package com.yongfill.server.domain.auth.dto;

import com.yongfill.server.domain.member.entity.Role;
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
    private Role role;

    @Builder
    public LoginAccessTokenDto(Long memberId, String accessToken, String refreshToken, String tokenType, Role role) {
        this.id = memberId;
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.role = role;
    }
}
