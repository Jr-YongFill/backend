package com.yongfill.server.domain.question.service;

import com.yongfill.server.domain.member.entity.Member;
import com.yongfill.server.domain.member.repository.MemberJpaRepository;
import com.yongfill.server.domain.question.dto.InterviewQuestionDto;
import com.yongfill.server.domain.question.entity.InterviewQuestion;
import com.yongfill.server.domain.question.repository.InterviewQuestionJpaRepository;
import com.yongfill.server.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.yongfill.server.global.common.response.error.ErrorCode.INVALID_MEMBER;
import static com.yongfill.server.global.common.response.error.ErrorCode.INVALID_QUESTION;

@Service
@RequiredArgsConstructor
public class InterviewQuestionService {
    private final InterviewQuestionJpaRepository interviewQuestionJpaRepository;
    private final MemberJpaRepository memberJpaRepository;

    @Transactional
    public InterviewQuestionDto.QuestionInsertResponseDto insertInterviewQuestion(InterviewQuestionDto.QuestionInsertRequestDto requestDto, Long memberId) {
        Member member = memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(INVALID_MEMBER));

        InterviewQuestion question = InterviewQuestion.builder()
                .interviewShow("N")
                .member(member)
                .question(requestDto.getQuestion())
                .createDate(LocalDateTime.now())
                .build();

        interviewQuestionJpaRepository.save(question);

        return InterviewQuestionDto.QuestionInsertResponseDto.toDto(question);
    }

    @Transactional
    public void updateQuestionState(Long questionId) {
        InterviewQuestion question = interviewQuestionJpaRepository.findById(questionId)
                .orElseThrow(() -> new CustomException(INVALID_QUESTION));

        question.updateInterviewShow();
    }
}
