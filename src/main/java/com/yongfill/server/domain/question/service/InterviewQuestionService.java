package com.yongfill.server.domain.question.service;

import com.yongfill.server.domain.member.entity.Member;
import com.yongfill.server.domain.member.repository.MemberJpaRepository;
import com.yongfill.server.domain.question.dto.InterviewQuestionDto;
import com.yongfill.server.domain.question.entity.InterviewQuestion;
import com.yongfill.server.domain.question.repository.InterviewQuestionJpaRepository;
import com.yongfill.server.domain.question.repository.InterviewQuestionQueryDSLRepository;
import com.yongfill.server.domain.stack.entity.QuestionStack;
import com.yongfill.server.domain.stack.repository.QuestionStackJpaRepository;
import com.yongfill.server.domain.stack.service.QuestionStackService;
import com.yongfill.server.domain.vote.entity.CountVote;
import com.yongfill.server.domain.vote.repository.CountVoteJpaRepository;
import com.yongfill.server.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.yongfill.server.global.common.response.error.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class InterviewQuestionService {
    private final InterviewQuestionJpaRepository interviewQuestionJpaRepository;
    private final InterviewQuestionQueryDSLRepository interviewQuestionQueryDSLRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final QuestionStackJpaRepository questionStackJpaRepository;
    private final CountVoteJpaRepository countVoteJpaRepository;

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

        List<QuestionStack> stacks = questionStackJpaRepository.findAll();
        List<CountVote> countVotes = stacks.stream()
                .map(stack ->
                    CountVote.builder()
                                    .count(0L)
                                    .questionStack(stack)
                                    .interviewQuestion(question)
                                    .build()
                ).toList();

        countVoteJpaRepository.saveAll(countVotes);

        return InterviewQuestionDto.QuestionInsertResponseDto.toDto(question);
    }

    @Transactional
    public void updateQuestionState(Long questionId) {
        InterviewQuestion question = interviewQuestionJpaRepository.findById(questionId)
                .orElseThrow(() -> new CustomException(INVALID_QUESTION));

        question.updateInterviewShow();
    }

    @Transactional
    public void deleteQuestion(Long questionId) {
        interviewQuestionJpaRepository.deleteById(questionId);
    }

    @Transactional
    public void updateQuestionStack(Long questionId, Long stackId) {
        QuestionStack stack = questionStackJpaRepository.findById(stackId)
                .orElseThrow(() -> new CustomException(INVALID_STACK));

        InterviewQuestion question = interviewQuestionJpaRepository.findById(questionId)
                .orElseThrow(() -> new CustomException(INVALID_QUESTION));

        question.updateQuestionStack(stack);
    }

    @Transactional
    public List<InterviewQuestionDto.QuestionRandomResponseDto> findQuestionRandomByStacks(List<Long> stackIds, Long size) {
        List<QuestionStack> stacks = questionStackJpaRepository.findAllById(stackIds);
        List<InterviewQuestion> questions = interviewQuestionQueryDSLRepository.findQuestionRandomByStacks(stacks, size);

        return questions.stream()
                .map(InterviewQuestionDto.QuestionRandomResponseDto::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void addQuestionStack(Long questionId, Long stackId) {
        InterviewQuestion question = interviewQuestionJpaRepository.findById(questionId)
                .orElseThrow(() -> new CustomException(INVALID_QUESTION));

        QuestionStack stack = questionStackJpaRepository.findById(stackId)
                .orElseThrow(() -> new CustomException(INVALID_STACK));

        Member createMember = question.getMember();

        question.updateQuestionStack(stack);
        question.updateInterviewShow();

        createMember.urgentCredit(10);
    }
}
