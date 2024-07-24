package com.yongfill.server.domain.stack.api;

import com.yongfill.server.domain.stack.dto.QuestionStackDto;
import com.yongfill.server.domain.stack.entity.QuestionStack;
import com.yongfill.server.domain.stack.service.QuestionStackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
