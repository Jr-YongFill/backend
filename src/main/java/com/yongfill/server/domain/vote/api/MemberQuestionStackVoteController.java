package com.yongfill.server.domain.vote.api;

import com.yongfill.server.domain.question.dto.InterviewQuestionDto;
import com.yongfill.server.domain.question.entity.InterviewQuestion;
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

@RestController
@RequiredArgsConstructor
public class MemberQuestionStackVoteController {

  private final MemberQuestionStackVoteService memberQuestionStackVoteService;
  private final JwtTokenProvider jwtTokenProvider;

  @PostMapping("/api/questions/{question_id}/votes")
  public ResponseEntity<Void> vote(@PathVariable("question_id") Long questionId,
      @RequestBody MemberQuestionStackVoteDto.VoteRequestDto requestDto,
      @RequestHeader("Authorization") String accessToken) {
    Long memberId = jwtTokenProvider.getUserIdFromToken(accessToken.substring(7));
    HttpStatus status = HttpStatus.CREATED;
    memberQuestionStackVoteService.vote(memberId, requestDto.getStackId(), questionId);

    return new ResponseEntity<>(status);
  }


  /**
   * Stack 정보와 질문의 투표 정보를 요청, accessToken에서 memberId 파싱함
   *
   * @param pageRequest {@link PageRequestDTO} 페이지네이션 를보를 담은 DTO 객체
   * @param accessToken {@link String} accessToken
   * @return service 계층에서 반환된 DTO가 ResponseEntity에 담겨 반환 됨
   * @author hg_yellow
   */
  @GetMapping("/api/votes")
  public ResponseEntity<InterviewQuestionDto.QuestionVoteResponseDto> getVoteInfos(
      PageRequestDTO pageRequest,
      @RequestHeader("Authorization") String accessToken) {
    Long memberId = jwtTokenProvider.getUserIdFromToken(accessToken.substring(7));
    HttpStatus status = HttpStatus.OK;

    InterviewQuestionDto.QuestionVoteResponseDto responseDto = memberQuestionStackVoteService.getVoteInfos(
        memberId, pageRequest);

    return new ResponseEntity<>(responseDto, status);
  }
}
