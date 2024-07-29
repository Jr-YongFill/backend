package com.yongfill.server.domain.auth.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuthRequestDTO {
    private String nickname;
    private String email;
    private String password;
}