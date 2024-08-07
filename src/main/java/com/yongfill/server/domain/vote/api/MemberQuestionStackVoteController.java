package com.yongfill.server.domain.vote.api;

import com.yongfill.server.domain.question.dto.InterviewQuestionDto;
import com.yongfill.server.domain.question.entity.InterviewQuestion;
import com.yongfill.server.domain.question.repository.InterviewQuestionJpaRepository;
import com.yongfill.server.domain.question.service.InterviewQuestionService;
import com.yongfill.server.domain.vote.dto.MemberQuestionStackVoteDto;
import com.yongfill.server.domain.vote.service.MemberQuestionStackVoteService;
import com.yongfill.server.global.common.dto.PageRequestDTO;
import com.yongfill.server.global.common.dto.PageResponseDTO;
import com.yongfill.server.global.config.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberQuestionStackVoteController {
    private final MemberQuestionStackVoteService memberQuestionStackVoteService;
    private final JwtTokenProvider jwtTokenProvider;
    private final InterviewQuestionJpaRepository interviewQuestionJpaRepository;
    private final InterviewQuestionService interviewQuestionService;

    @PostMapping("/api/questions/{question_id}/votes")
    public ResponseEntity<Void> vote(@PathVariable("question_id") Long questionId,
                                     @RequestBody MemberQuestionStackVoteDto.VoteRequestDto requestDto,
                                     @RequestHeader("Authorization") String accessToken) {
        Long memberId = jwtTokenProvider.getUserIdFromToken(accessToken.substring(7));
        HttpStatus status = HttpStatus.CREATED;
        memberQuestionStackVoteService.vote(memberId, requestDto.getStackId(), questionId);

        return new ResponseEntity<>(status);
    }

    @GetMapping("/api/votes")
    public ResponseEntity<InterviewQuestionDto.QuestionVoteResponseDto> getVoteInfos(PageRequestDTO pageRequest,
                                                                                     @RequestHeader("Authorization") String accessToken) {
        Long memberId = jwtTokenProvider.getUserIdFromToken(accessToken.substring(7));
        HttpStatus status = HttpStatus.OK;
        InterviewQuestionDto.QuestionVoteResponseDto responseDto = memberQuestionStackVoteService.getVoteInfos(memberId, pageRequest);

        return new ResponseEntity<>(responseDto, status);
    }

    @GetMapping("/api/v2/votes")
    public ResponseEntity<List<InterviewQuestionDto.QuestionVoteResponseDto.QuestionPageDto>> getVoteInfosV2(PageRequestDTO pageRequest,
                                                                                                           @RequestHeader("Authorization") String accessToken) {
        Long memberId = jwtTokenProvider.getUserIdFromToken(accessToken.substring(7));
        HttpStatus status = HttpStatus.OK;
        List<InterviewQuestionDto.QuestionVoteResponseDto.QuestionPageDto> responseDto = memberQuestionStackVoteService.getVoteInfosV2(memberId, pageRequest);

        return new ResponseEntity<>(responseDto, status);
    }

    @PostMapping("/api/eval/test")
    public void insertTestData() {
        interviewQuestionService.insert();
    }
}
