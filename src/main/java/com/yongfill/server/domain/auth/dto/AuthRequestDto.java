package com.yongfill.server.domain.auth.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuthRequestDto {
    private String email;
    private String password;
}