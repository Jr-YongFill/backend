package com.yongfill.server.domain.vote.service;

import com.yongfill.server.domain.member.entity.Member;
import com.yongfill.server.domain.member.entity.Role;
import com.yongfill.server.domain.member.repository.MemberJpaRepository;
import com.yongfill.server.domain.question.dto.InterviewQuestionDto;
import com.yongfill.server.domain.question.entity.InterviewQuestion;
import com.yongfill.server.domain.question.repository.InterviewQuestionJpaRepository;
import com.yongfill.server.domain.question.service.InterviewQuestionService;
import com.yongfill.server.domain.stack.dto.QuestionStackDto;
import com.yongfill.server.domain.stack.entity.QuestionStack;
import com.yongfill.server.domain.stack.repository.QuestionStackJpaRepository;
import com.yongfill.server.domain.vote.entity.CountVote;
import com.yongfill.server.domain.vote.repository.CountVoteJpaRepository;
import com.yongfill.server.global.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.yongfill.server.global.common.response.error.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
class MemberQuestionStackVoteServiceTest {
    @Autowired
    MemberJpaRepository memberJpaRepository;
    @Autowired
    InterviewQuestionJpaRepository interviewQuestionJpaRepository;
    @Autowired
    QuestionStackJpaRepository questionStackJpaRepository;
    @Autowired
    InterviewQuestionService interviewQuestionService;
    @Autowired
    CountVoteJpaRepository countVoteJpaRepository;
    @Autowired
    MemberQuestionStackVoteService memberQuestionStackVoteService;

    @Test
    @DisplayName("문제 투표")
    void 문제_투표() {
        Member member = createMember("사람", Role.USER);
        Long memberId = member.getId();
        List<QuestionStack> stacks = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            stacks.add(createStack(String.valueOf(i)));
        }
        InterviewQuestionDto.QuestionInsertRequestDto questionInsertRequestDto = InterviewQuestionDto.QuestionInsertRequestDto.builder()
                .question("문제1")
                .build();
        Long questionId = interviewQuestionService.insertInterviewQuestion(questionInsertRequestDto, memberId).getQuestionId();

        for (int i = 0; i < stacks.size(); i++) {
            QuestionStack stack = stacks.get(i);
            if (i % 2 == 0) {
                Long stackId = stack.getId();
                memberQuestionStackVoteService.vote(memberId, stackId, questionId);
            }
        }

        List<CountVote> countVotes = countVoteJpaRepository.findAll();

        assertEquals(5, countVotes.stream()
                .filter(countVote -> countVote.getCount() == 1)
                .count());
    }

    @Test
    @DisplayName("중복 투표")
    void 중복_투표() {
        Member member = createMember("사람", Role.USER);
        Long memberId = member.getId();
        List<QuestionStack> stacks = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            stacks.add(createStack(String.valueOf(i)));
        }
        InterviewQuestionDto.QuestionInsertRequestDto questionInsertRequestDto = InterviewQuestionDto.QuestionInsertRequestDto.builder()
                .question("문제1")
                .build();
        Long questionId = interviewQuestionService.insertInterviewQuestion(questionInsertRequestDto, memberId).getQuestionId();
        Long stackId = stacks.get(0).getId();
        memberQuestionStackVoteService.vote(memberId, stackId, questionId);

        Throwable exception = assertThrowsExactly(
                CustomException.class,
                () -> memberQuestionStackVoteService.vote(memberId, stackId, questionId)
        );

        assertEquals(MEMBER_ALREADY_VOTE.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("없는 사람 투표")
    void 없는_사람_투표() {
        Member member = createMember("사람", Role.USER);
        Long memberId = member.getId();
        List<QuestionStack> stacks = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            stacks.add(createStack(String.valueOf(i)));
        }
        InterviewQuestionDto.QuestionInsertRequestDto questionInsertRequestDto = InterviewQuestionDto.QuestionInsertRequestDto.builder()
                .question("문제1")
                .build();
        Long questionId = interviewQuestionService.insertInterviewQuestion(questionInsertRequestDto, memberId).getQuestionId();
        Long stackId = stacks.get(0).getId();

        Throwable exception = assertThrowsExactly(
                CustomException.class,
                () -> memberQuestionStackVoteService.vote(memberId+1, stackId, questionId)
        );

        assertEquals(INVALID_MEMBER.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("없는 질문 투표")
    void 없는_질문_투표() {
        Member member = createMember("사람", Role.USER);
        Long memberId = member.getId();
        List<QuestionStack> stacks = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            stacks.add(createStack(String.valueOf(i)));
        }
        InterviewQuestionDto.QuestionInsertRequestDto questionInsertRequestDto = InterviewQuestionDto.QuestionInsertRequestDto.builder()
                .question("문제1")
                .build();
        Long questionId = interviewQuestionService.insertInterviewQuestion(questionInsertRequestDto, memberId).getQuestionId();
        Long stackId = stacks.get(0).getId();

        Throwable exception = assertThrowsExactly(
                CustomException.class,
                () -> memberQuestionStackVoteService.vote(memberId, stackId, questionId+1)
        );

        assertEquals(INVALID_QUESTION.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("없는 스택 투표")
    void 없는_스택_투표() {
        Member member = createMember("사람", Role.USER);
        Long memberId = member.getId();
        List<QuestionStack> stacks = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            stacks.add(createStack(String.valueOf(i)));
        }
        InterviewQuestionDto.QuestionInsertRequestDto questionInsertRequestDto = InterviewQuestionDto.QuestionInsertRequestDto.builder()
                .question("문제1")
                .build();
        Long questionId = interviewQuestionService.insertInterviewQuestion(questionInsertRequestDto, memberId).getQuestionId();
        Long stackId = stacks.get(0).getId();

        Throwable exception = assertThrowsExactly(
                CustomException.class,
                () -> memberQuestionStackVoteService.vote(memberId, stackId+stacks.size()+1, questionId)
        );

        assertEquals(INVALID_STACK.getMessage(), exception.getMessage());
    }

    private Member createMember(String name, Role role) {
        Member member = Member.builder()
                .email(name)
                .credit(0L)
                .attachmentFileName(name)
                .attachmentFileSize(0L)
                .attachmentOriginalFileName(name)
                .filePath(name)
                .nickname(name)
                .password(name)
                .createDate(LocalDateTime.now())
                .role(role)
                .build();
        memberJpaRepository.save(member);

        return member;
    }

    private InterviewQuestion createInterviewQuestion(Member member, String state, String content, QuestionStack stack) {
        InterviewQuestion interviewQuestion = InterviewQuestion.builder()
                .interviewShow(state)
                .createDate(LocalDateTime.now())
                .member(member)
                .question(content)
                .questionStack(stack)
                .build();
        interviewQuestionJpaRepository.save(interviewQuestion);

        return interviewQuestion;
    }

    private QuestionStack createStack(String name) {
        QuestionStack stack = QuestionStack.builder()
                .price(0L)
                .stackName(name)
                .description(name)
                .build();
        questionStackJpaRepository.save(stack);

        return stack;
    }
}