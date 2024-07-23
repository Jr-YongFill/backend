package com.yongfill.server.domain.question.dto;

import com.yongfill.server.domain.question.entity.InterviewQuestion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class InterviewQuestionDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class QuestionInsertRequestDto {
        String question;
    }

    @AllArgsConstructor
    @Builder
    @Data
    public static class QuestionInsertResponseDto {
        Long questionId;

        public static QuestionInsertResponseDto toDto(InterviewQuestion interviewQuestion) {
            return QuestionInsertResponseDto.builder()
                    .questionId(interviewQuestion.getId())
                    .build();
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QuestionPatchStackRequestDto {
        private Long stackId;
    }
}
