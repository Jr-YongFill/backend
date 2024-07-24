package com.yongfill.server.domain.vote.api;

import com.yongfill.server.domain.vote.dto.MemberQuestionStackVoteDto;
import com.yongfill.server.domain.vote.service.MemberQuestionStackVoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
