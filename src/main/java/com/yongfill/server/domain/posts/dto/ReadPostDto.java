package com.yongfill.server.domain.posts.dto;

import com.yongfill.server.domain.comments.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.List;

public class ReadPostDto {

    @Data
    @Builder
    @AllArgsConstructor
    public static class ResponseDto {

        private String title;
        private String categoryName;
        private String content;
        private String writerName;
        private LocalDateTime createTime;
        private LocalDateTime lastUpdateTime;
        private Long likeCount;
        private Long viewCount;
        private String updateYn;

    }
}
