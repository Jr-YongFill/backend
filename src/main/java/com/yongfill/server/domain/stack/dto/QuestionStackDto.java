package com.yongfill.server.domain.stack.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.yongfill.server.domain.stack.entity.QuestionStack;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class QuestionStackDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StackInsertRequestDto {
        private String stackName;
        private Long price;
        private String description;

        public QuestionStack toEntity() {
            return QuestionStack.builder()
                    .description(description)
                    .price(price)
                    .stackName(stackName)
                    .build();
        }
    }

    @Data
    @Builder
    public static class StackInsertResponseDto {
        private Long stackId;

        public static StackInsertResponseDto toDto(QuestionStack stack) {
            return QuestionStackDto.StackInsertResponseDto.builder()
                    .stackId(stack.getId())
                    .build();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StackUpdateRequestDto {
        private String stackName;
        private Long price;
        private String description;
    }

    @Data
    @Builder
    public static class StackResponseDto {
        private Long id;
        private String stackName;
        private Long price;
        private String description;
        private Boolean isPurchase;

        @QueryProjection
        public StackResponseDto(Long id, String stackName, Long price, String description, Boolean isPurchase) {
            this.id = id;
            this.stackName = stackName;
            this.price = price;
            this.description = description;
            this.isPurchase = isPurchase;
        }
    }
}
