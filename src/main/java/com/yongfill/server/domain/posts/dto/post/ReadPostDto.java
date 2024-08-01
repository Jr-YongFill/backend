package com.yongfill.server.domain.posts.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

public class ReadPostDto {

    //게시글 상세 조회
    @Data
    @Builder
    @AllArgsConstructor
    public static class DetailResponseDto {
        private Long postId;
        private Long memberId;
        private String title;
        private String category;
        private String content;
        private String writerName;
        private LocalDateTime createTime;
        private LocalDateTime lastUpdateTime;
        private Long likeCount;
        private Long viewCount;
        private String updateYn;
        private boolean isLiked;//좋아요 여부
        private String filePath;//프로필 이미지 경로

    }

    //검색 데이터 조회
    @Data
    @Builder
    @AllArgsConstructor
    public static class SimpleResponseDto {

        private Long postId;
        private String title;
        private String writerName;
        private LocalDateTime createTime;
        private LocalDateTime lastUpdateTime;

    }

    @Data
    @Builder
    public static class MainPageResponseDto{
        private String category;
        private List<SimpleResponseDto> postList;
    }

}
