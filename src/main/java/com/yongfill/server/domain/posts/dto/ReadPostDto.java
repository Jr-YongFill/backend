package com.yongfill.server.domain.posts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

public class ReadPostDto {

    //게시글 상세 조회
    @Data
    @Builder
    @AllArgsConstructor
    public static class DetailResponseDto {

        private String title;
        private String category;
        private String content;
        private String writerName;
        private LocalDateTime createTime;
        private LocalDateTime lastUpdateTime;
        private Long likeCount;
        private Long viewCount;
        private String updateYn;

    }

    //검색 데이터 조회
    @Data
    @Builder
    @AllArgsConstructor
    public static class SimpleResponseDto {

        private String title;
        private String writerName;
        private LocalDateTime createTime;
        private LocalDateTime lastUpdateTime;

    }


}
