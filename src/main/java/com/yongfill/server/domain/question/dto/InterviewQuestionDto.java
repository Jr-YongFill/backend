package com.yongfill.server.domain.question.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.yongfill.server.domain.question.entity.InterviewQuestion;
import com.yongfill.server.domain.answer.dto.MemberAnswerDTO;
import com.yongfill.server.domain.stack.entity.QuestionStack;
import com.yongfill.server.global.common.dto.PageResponseNoEntityDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @Data
    @Builder
    public static class QuestionRandomResponseDto {
        private Long questionId;
        private String question;

        public static QuestionRandomResponseDto toDto(InterviewQuestion interviewQuestion) {
            return QuestionRandomResponseDto.builder()
                    .questionId(interviewQuestion.getId())
                    .question(interviewQuestion.getQuestion())
                    .build();
        }
    }


    @Data
    @Builder
    @NoArgsConstructor
    public static class QuestionMemberAnswerResponseDTO {
        private Long id;
        private String question;
        private List<MemberAnswerDTO.MemberAnswerPageResponseDTO> memberAnswers;

        public QuestionMemberAnswerResponseDTO(Long id, String question , List<MemberAnswerDTO.MemberAnswerPageResponseDTO> memberAnswers) {
            this.id = id;
            this.question = question;
            this.memberAnswers = memberAnswers;
        }
    }



    @Data
    @Builder
    public static class QuestionVoteResponseDto {
        private PageResponseNoEntityDto<QuestionPageDto> pageResponseDTO;
        private List<StackInfoDto> stackInfoDtos;

        @Data
        @Builder
        public static class StackInfoDto{
            private Long stackId;
            private String stackName;
            private String stackDescription;

            public static StackInfoDto toDto(QuestionStack stack) {
                return StackInfoDto.builder()
                        .stackId(stack.getId())
                        .stackDescription(stack.getDescription())
                        .stackName(stack.getStackName())
                        .build();
            }
        }

        @Data
        @Builder
        public static class QuestionPageDto {
            private Long questionId;
            private String question;
            private String createMember;
            private Long myVoteStackId;
            private List<StackDto> stackDtos;

            @QueryProjection
            public QuestionPageDto(Long questionId, String question, String createMember, Long myVoteStackId, List<StackDto> stackDtos) {
                this.questionId = questionId;
                this.question = question;
                this.createMember = createMember;
                this.myVoteStackId = myVoteStackId;
                this.stackDtos = stackDtos;
            }



            @Data
            @Builder
            public static class StackDto {
                private Long stackId;
                private Long voteCount;

                @QueryProjection
                public StackDto(Long stackId, Long voteCount) {
                    this.stackId = stackId;
                    this.voteCount = voteCount;
                }
            }

        }

    }
}