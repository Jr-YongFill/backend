package com.yongfill.server.domain.vote.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class MemberQuestionStackVoteDto {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class VoteRequestDto {
        private Long stackId;
    }
}
