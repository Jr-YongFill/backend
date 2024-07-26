package com.yongfill.server.domain.member.dto;

import com.yongfill.server.domain.member.entity.Member;
import com.yongfill.server.domain.member.entity.Role;
import lombok.*;

import java.time.LocalDateTime;

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public class MemberResponseDTO {
        private Long id;
        private String nickname;
        private String email;
        private Role role;
        private LocalDateTime createDate;
        private Long credit;
        private String refreshToken;
        private String attachmentFileName;
        private String attachmentOriginalFileName;
        private Long attachmentFileSize;
        private String filePath;


        public MemberResponseDTO(Member entity) {
            this.id = entity.getId();
            this.nickname = entity.getNickname();
            this.email = entity.getEmail();
            this.role = entity.getRole();
            this.createDate = entity.getCreateDate();
            this.credit = entity.getCredit();
            this.refreshToken = entity.getRefreshToken();
            this.attachmentFileName = entity.getAttachmentFileName();
            this.attachmentFileSize = entity.getAttachmentFileSize();
            this.filePath = entity.getFilePath();
            this.attachmentOriginalFileName = entity.getAttachmentOriginalFileName();
        }
    }

