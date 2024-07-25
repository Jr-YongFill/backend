package com.yongfill.server.domain.member.dto;

import com.yongfill.server.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class MemberLoginDTO {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MemberLoginResponseDTO {
        private Long id;
        private String nickname;
        private String email;

        public MemberLoginResponseDTO(Member member){
            this.id = member.getId();
            this.nickname = member.getNickname();
            this.email = member.getEmail();
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MemberLoginRequestDTO {
        private Long id;
        private String nickname;
        private String email;
        private String password;



    }
}
