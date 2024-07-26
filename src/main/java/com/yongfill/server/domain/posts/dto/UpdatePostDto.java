package com.yongfill.server.domain.posts.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

public class UpdatePostDto {

    @Builder
    @Data
    public static class RequestDto{
        private String content;
        private String title;
        private String category;
    }

    @Builder
    @Data
    public static class ResponseDto{

        private HttpStatus status;
        private String message;
    }
}
