package com.yongfill.server.domain.answer.api;

import com.yongfill.server.domain.answer.dto.MemberAnswerDTO;
import com.yongfill.server.domain.answer.entity.MemberAnswer;
import com.yongfill.server.domain.answer.service.MemberAnswerService;
import lombok.RequiredArgsConstructor;
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

}
