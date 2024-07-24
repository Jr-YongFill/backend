package com.yongfill.server.domain.member.dto;

import com.yongfill.server.domain.member.entity.Member;
import com.yongfill.server.domain.member.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class MemberDTO {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberResponseDTO {
        private Long id;
        private String nickname;
        private String email;
        private String password;
        private Role role;
        private LocalDateTime createDate;
        private Long credit;

        public MemberResponseDTO(Member member) {
            this.id = member.getId();
            this.nickname = member.getNickname();
            this.email = member.getEmail();
            this.password = member.getPassword();
            this.credit = member.getCredit();
            this.createDate = member.getCreateDate();
            this.role = member.getRole();
            }
    }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class MemberRequestDTO {
            private Long id;
            private String nickname;
            private String email;
            private String password;
            private Role role;
            private LocalDateTime createDate;
            private Long credit;
            private String filePath;
            private String attachmentFileName;
            private String attachmentOriginalFileName;
            private Long attachmentFileSize;
            private String refreshToken;


        }
}
