package com.yongfill.server.domain.vote.api;

import com.yongfill.server.domain.question.dto.InterviewQuestionDto;
import com.yongfill.server.domain.question.entity.InterviewQuestion;
import com.yongfill.server.domain.vote.dto.MemberQuestionStackVoteDto;
import com.yongfill.server.domain.vote.service.MemberQuestionStackVoteService;
import com.yongfill.server.global.common.dto.PageRequestDTO;
import com.yongfill.server.global.common.dto.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberQuestionStackVoteController {
    private final MemberQuestionStackVoteService memberQuestionStackVoteService;

    @PostMapping("/api/questions/{question_id}/votes")
    public ResponseEntity<Void> vote(@PathVariable("question_id") Long questionId, @RequestBody MemberQuestionStackVoteDto.VoteRequestDto requestDto) {
        Long memberId = 1L;
        HttpStatus status = HttpStatus.CREATED;
        memberQuestionStackVoteService.vote(memberId, requestDto.getStackId(), questionId);

        return new ResponseEntity<>(status);
    }

    @GetMapping("/api/votes")
    public ResponseEntity<InterviewQuestionDto.QuestionVoteResponseDto> getVoteInfos(PageRequestDTO pageRequest) {
        Long memberId = 1L;
        HttpStatus status = HttpStatus.OK;
        InterviewQuestionDto.QuestionVoteResponseDto responseDto = memberQuestionStackVoteService.getVoteInfos(memberId, pageRequest);

        return new ResponseEntity<>(responseDto, status);
    }
}
