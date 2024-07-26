package com.yongfill.server.domain.comments.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class CommentDTO {


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CommentPageResponseDTO {
        private Long id;
        private String content;
        private LocalDateTime createDate;
        private LocalDateTime updateDate;
        private String updateYn;

        private Long memberId;
        private String memberNickname;
        private String filePath;
        private String attachmentFileName;
        private String attachmentOriginalFilename;
        private Long attachmentFileSize;

    }
}
