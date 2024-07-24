package com.yongfill.server.domain.posts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import java.util.List;

public class ReadPostDto {

    @Data
    @Builder
    @AllArgsConstructor
    public static class ResponseDto {

        private String title;
        private String categoryName;
        private String content;

    }
}
