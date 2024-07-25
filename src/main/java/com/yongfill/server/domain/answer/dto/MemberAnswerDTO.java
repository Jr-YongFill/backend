package com.yongfill.server.domain.answer.dto;

import com.yongfill.server.domain.answer.entity.InterviewMode;
import lombok.*;


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
        private Long Id;
        private Long memberId;
        private Long questionId;
        private String memberAnswer;
        private String gptAnswer;
        private String interviewMode;
    }
}
