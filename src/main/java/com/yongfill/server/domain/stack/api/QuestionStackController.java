package com.yongfill.server.domain.stack.api;

import com.yongfill.server.domain.stack.dto.QuestionStackDto;
import com.yongfill.server.domain.stack.entity.QuestionStack;
import com.yongfill.server.domain.stack.service.QuestionStackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class QuestionStackController {
    private final QuestionStackService questionStackService;

    @PostMapping("/api/stacks")
    public ResponseEntity<QuestionStackDto.StackInsertResponseDto> insertStack(@RequestBody QuestionStackDto.StackInsertRequestDto requestDto) {
        HttpStatus status = HttpStatus.CREATED;
        QuestionStackDto.StackInsertResponseDto responseDto = questionStackService.insertStack(requestDto);

        return new ResponseEntity<>(responseDto, status);
    }

    @PatchMapping("api/stacks/{stack_id}")
    public ResponseEntity<Void> updateStack(@RequestBody QuestionStackDto.StackUpdateRequestDto requestDto, @PathVariable("stack_id") Long stackId) {
        HttpStatus status = HttpStatus.OK;
        questionStackService.updateStack(requestDto, stackId);

        return new ResponseEntity<>(status);
    }
}
