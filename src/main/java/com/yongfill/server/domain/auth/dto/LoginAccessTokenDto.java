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
    private String nickName;

    @Builder
    public LoginAccessTokenDto(Long memberId, String accessToken, String refreshToken, String tokenType, Role role, String nickName) {
        this.id = memberId;
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.role = role;
        this.nickName = nickName;
    }
}
