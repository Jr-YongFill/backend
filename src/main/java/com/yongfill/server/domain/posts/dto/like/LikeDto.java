package com.yongfill.server.domain.posts.dto.like;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

public class LikeDto {

    @Data
    @Builder
    public static class PostRequestDto{
        private Long postId;
    }

    @Data
    @Builder
    public static class PostResponseDto {
        private HttpStatus status;
        private String message;
    }
}
