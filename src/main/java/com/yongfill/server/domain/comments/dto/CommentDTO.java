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
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
        private String updateYn;
        private Long memberId;
        private String memberNickname;
        private String filePath;
        private String attachmentFileName;
        private String attachmentOriginalFilename;
        private Long attachmentFileSize;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CommentCreateRequestDTO {
        private String content;
        private Long memberId;
        private Long postId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CommentCreateResponseDTO {
        private Long id;
        private String content;
        private Long postId;
        private LocalDateTime createTime;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CommentMemberPageResponseDTO {
        private Long id;
        private Long postId;
        private String content;
        private LocalDateTime createTime;
        private String updateYn;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CommentUpdateRequestDTO {
        private Long id;
        private String content;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CommentUpdateResponseDTO {
        private Long id;
        private String content;
        private LocalDateTime updateTime;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CommentDeleteResponseDTO {
        private Long id;

    }
}
