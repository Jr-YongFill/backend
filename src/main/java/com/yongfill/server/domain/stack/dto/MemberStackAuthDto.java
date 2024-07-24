package com.yongfill.server.domain.stack.dto;

import com.yongfill.server.domain.stack.entity.MemberStackAuth;
import lombok.Builder;
import lombok.Data;

public class MemberStackAuthDto {

    @Data
    @Builder
    public static class AuthResponseDto {
        private Long authId;

        public static AuthResponseDto toDto(MemberStackAuth auth) {
            return AuthResponseDto.builder()
                    .authId(auth.getId())
                    .build();
        }
    }

}
