package com.yongfill.server.domain.posts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
public class PostDto {

    @Data
    @Builder
    @AllArgsConstructor
    public static class PostRequestDto {

        private String title;
        private String categoryName;
        private String content;
        private LocalDateTime createDate;
        private LocalDateTime updateDate;
        private Long viewCount;
        private Long likeCount;
        private String updateYn;
        private String memberName;
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class PostResponseDto{

        private String title;
        private String categoryName;
        private String content;
        private LocalDateTime createDate;
        private LocalDateTime updateDate;
        private Long viewCount;
        private Long likeCount;
        private String updateYn;
        private String memberName;
    }
}
