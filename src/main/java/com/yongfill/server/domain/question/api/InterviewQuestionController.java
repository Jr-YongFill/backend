package com.yongfill.server.domain.question.api;

import com.yongfill.server.domain.question.dto.InterviewQuestionDto;
import com.yongfill.server.domain.question.service.InterviewQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InterviewQuestionController {
    private final InterviewQuestionService interviewQuestionService;

    @PostMapping("/api/questions")
    public ResponseEntity<InterviewQuestionDto.QuestionInsertResponseDto> insertQuestion(@RequestBody InterviewQuestionDto.QuestionInsertRequestDto requestDto) {
        Long memberId = 1L;
        HttpStatus status = HttpStatus.CREATED;
        InterviewQuestionDto.QuestionInsertResponseDto responseDto = interviewQuestionService.insertInterviewQuestion(requestDto, memberId);

        return new ResponseEntity<>(responseDto, status);
    }
}
