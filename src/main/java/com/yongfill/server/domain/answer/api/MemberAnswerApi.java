package com.yongfill.server.domain.answer.api;

import com.yongfill.server.domain.answer.dto.MemberAnswerDTO;
import com.yongfill.server.domain.answer.entity.MemberAnswer;
import com.yongfill.server.domain.answer.service.MemberAnswerService;
import com.yongfill.server.domain.question.dto.InterviewQuestionDto;
import com.yongfill.server.global.common.dto.PageRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class MemberAnswerApi {

    private final MemberAnswerService memberAnswerService;

    @PostMapping("/api/members/{member_id}/answers")
    public ResponseEntity<MemberAnswerDTO.MemberAnswerResponseDTO> addAnswer(@PathVariable("member_id") Long member_id,
                                                                             @RequestBody MemberAnswerDTO.MemberAnswerRequestDTO memberAnswerRequestDTO) {

        memberAnswerRequestDTO.setMemberId(member_id);
        HttpStatus status = HttpStatus.CREATED;

        MemberAnswer newAnswer = memberAnswerService.addMemberAnswer(memberAnswerRequestDTO);

        return new ResponseEntity<>(memberAnswerService.toDto(newAnswer), status);

    }

    @GetMapping("/api/members/{member_id}/stacks/{stack_id}/answers")
    public ResponseEntity<Page<InterviewQuestionDto.QuestionMemberAnswerResponseDTO>> findAnswers(@PathVariable("member_id") Long memberId,
                                                                                     @PathVariable("stack_id") Long stackId,
                                                                                     @RequestParam int page,
                                                                                     @RequestParam int size){

        HttpStatus status = HttpStatus.OK;

        PageRequestDTO pageDTO = new PageRequestDTO(page, size);
        MemberAnswerDTO.MemberAnswerPageRequestDTO requestDTO =
                MemberAnswerDTO.MemberAnswerPageRequestDTO.builder()
                        .pageRequest(pageDTO)
                        .memberId(memberId)
                        .stackId(stackId)
                        .build();

        Page<InterviewQuestionDto.QuestionMemberAnswerResponseDTO> result = memberAnswerService.findQuestionsMemberAnswers(requestDTO);

        return new ResponseEntity<>(result, status);
    }

    @GetMapping("/api/members/{member_id}/answers")
    public ResponseEntity<MemberAnswerDTO.MemberAnswerCountDto> findCountTodayAnswer(@PathVariable("member_id")Long memberId){
        HttpStatus status = HttpStatus.OK;
        MemberAnswerDTO.MemberAnswerCountDto result = memberAnswerService.countTodayAnswer(memberId);

        return new ResponseEntity<>(result,status);
    }
}
