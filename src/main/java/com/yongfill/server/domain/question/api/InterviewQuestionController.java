package com.yongfill.server.domain.question.api;

import com.yongfill.server.domain.question.dto.InterviewQuestionDto;
import com.yongfill.server.domain.question.service.InterviewQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/api/questions/{question_id}")
    public ResponseEntity<Void> updateQuestionState(@PathVariable("question_id") Long questionId) {
        HttpStatus status = HttpStatus.OK;
        interviewQuestionService.updateQuestionState(questionId);

        return new ResponseEntity<>(status);
    }

    @DeleteMapping("/api/questions/{question_id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable("question_id") Long questionId) {
        HttpStatus status = HttpStatus.NO_CONTENT;
        interviewQuestionService.deleteQuestion(questionId);

        return new ResponseEntity<>(status);
    }
}
