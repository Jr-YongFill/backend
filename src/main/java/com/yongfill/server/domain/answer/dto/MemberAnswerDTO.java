package com.yongfill.server.domain.answer.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.yongfill.server.domain.answer.entity.InterviewMode;
import com.yongfill.server.global.common.dto.PageRequestDTO;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;


public class MemberAnswerDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MemberAnswerRequestDTO {

        private Long memberId;
        private Long questionId;
        private String memberAnswer;
        private String gptAnswer;
        private String interviewMode;

    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MemberAnswerResponseDTO {
        private LocalDateTime createDate;
        private Long Id;
        private Long memberId;
        private Long questionId;
        private String memberAnswer;
        private String gptAnswer;
        private String interviewMode;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MemberAnswerPageRequestDTO {
        private Long memberId;
        private Long stackId;
        private PageRequestDTO pageRequest;

    }

    @Data
    @NoArgsConstructor
    public static class MemberAnswerPageResponseDTO {
        private Timestamp createDate;
        private Long id;
        private String memberAnswer;
        private String gptAnswer;
        private String interviewMode;

        public MemberAnswerPageResponseDTO(Timestamp createDate, Long id, String memberAnswer, String gptAnswer, String interviewMode) {
            this.createDate = createDate;
            this.id = id;
            this.memberAnswer = memberAnswer;
            this.gptAnswer = gptAnswer;
            this.interviewMode = interviewMode;
        }
    }




}
