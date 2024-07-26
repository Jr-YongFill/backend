package com.yongfill.server.domain.question.api;

import com.yongfill.server.domain.question.dto.InterviewQuestionDto;
import com.yongfill.server.domain.question.service.InterviewQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PatchMapping("/api/questions/{question_id}/state")
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

    @PatchMapping("/api/questions/{question_id}/stacks")
    public ResponseEntity<Void> updateQuestionStack(@PathVariable("question_id") Long questionId, @RequestBody InterviewQuestionDto.QuestionPatchStackRequestDto requestDto) {
        HttpStatus status = HttpStatus.OK;
        interviewQuestionService.updateQuestionStack(questionId, requestDto.getStackId());

        return new ResponseEntity<>(status);
    }

    @GetMapping("/api/questions")
    public ResponseEntity<List<InterviewQuestionDto.QuestionRandomResponseDto>> findQuestionRandomByStacks(@RequestParam("stack_id") List<Long> stackIds, @RequestParam("size") Long size) {
        HttpStatus status = HttpStatus.OK;
        List<InterviewQuestionDto.QuestionRandomResponseDto> responseDtos = interviewQuestionService.findQuestionRandomByStacks(stackIds, size);

        return new ResponseEntity<>(responseDtos, status);
    }

    @PatchMapping("/api/admin/questions/{question_id}")
    public ResponseEntity<Void> addQuestionStack(@PathVariable("question_id") Long questionId, @RequestBody InterviewQuestionDto.QuestionPatchStackRequestDto requestDto) {
        HttpStatus status = HttpStatus.OK;
        interviewQuestionService.addQuestionStack(questionId, requestDto.getStackId());

        return new ResponseEntity<>(status);
    }
}
