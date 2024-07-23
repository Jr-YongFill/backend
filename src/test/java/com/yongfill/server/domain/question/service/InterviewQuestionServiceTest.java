package com.yongfill.server.domain.question.service;

import com.yongfill.server.domain.member.entity.Member;
import com.yongfill.server.domain.member.entity.Role;
import com.yongfill.server.domain.member.repository.MemberJpaRepository;
import com.yongfill.server.domain.question.dto.InterviewQuestionDto;
import com.yongfill.server.domain.question.entity.InterviewQuestion;
import com.yongfill.server.domain.question.repository.InterviewQuestionJpaRepository;
import com.yongfill.server.domain.stack.entity.QuestionStack;
import com.yongfill.server.domain.stack.repository.QuestionStackJpaRepository;
import com.yongfill.server.global.exception.CustomException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.yongfill.server.global.common.response.error.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
class InterviewQuestionServiceTest {
    @Autowired
    MemberJpaRepository memberJpaRepository;
    @Autowired
    InterviewQuestionJpaRepository interviewQuestionJpaRepository;
    @Autowired
    InterviewQuestionService interviewQuestionService;
    @Autowired
    QuestionStackJpaRepository questionStackJpaRepository;

    @Test
    @DisplayName("문제 생성 테스트")
    public void 문제_생성_테스트() {
        Member member = Member.builder()
                .email("email")
                .credit(100L)
                .attachmentFileName("123")
                .attachmentFileSize(100L)
                .attachmentOriginalFileName("123")
                .filePath("123")
                .nickname("1")
                .password("123")
                .createDate(LocalDateTime.now())
                .role(Role.USER)
                .build();

        memberJpaRepository.save(member);

        Long memberId = member.getId();

        InterviewQuestionDto.QuestionInsertRequestDto requestDto = InterviewQuestionDto.QuestionInsertRequestDto
                .builder()
                .question("test 질문입니당~")
                .build();

        InterviewQuestionDto.QuestionInsertResponseDto responseDto = interviewQuestionService.insertInterviewQuestion(requestDto, memberId);

        InterviewQuestion question = interviewQuestionJpaRepository.findById(responseDto.getQuestionId())
                .orElseThrow(() -> new IllegalArgumentException());

        assertEquals(responseDto.getQuestionId(), question.getId());
    }

    @Test
    @DisplayName("없는 사용자가 문제 생성 테스트")
    public void 없는_사용자_예외() {
        Long memberId = 2L;

        InterviewQuestionDto.QuestionInsertRequestDto requestDto = InterviewQuestionDto.QuestionInsertRequestDto
                .builder()
                .question("test 질문입니당~")
                .build();

        Throwable exception = assertThrowsExactly(
                CustomException.class,
                () -> interviewQuestionService.insertInterviewQuestion(requestDto, memberId)
        );

        assertEquals(INVALID_MEMBER.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("질문 상태 수정 N에서 Y")
    public void 질문_상태_수정_N_to_Y() {
        Member member = Member.builder()
                .email("email")
                .credit(100L)
                .attachmentFileName("123")
                .attachmentFileSize(100L)
                .attachmentOriginalFileName("123")
                .filePath("123")
                .nickname("1")
                .password("123")
                .createDate(LocalDateTime.now())
                .role(Role.USER)
                .build();
        memberJpaRepository.save(member);

        InterviewQuestion interviewQuestion = InterviewQuestion.builder()
                .interviewShow("N")
                .createDate(LocalDateTime.now())
                .member(member)
                .question("test용 질문입니디.")
                .build();
        interviewQuestionJpaRepository.save(interviewQuestion);
        Long questionId = interviewQuestion.getId();

        interviewQuestionService.updateQuestionState(questionId);

        assertEquals("Y", interviewQuestion.getInterviewShow());
    }

    @Test
    @DisplayName("질문 상태 수정 Y에서 N")
    public void 질문_상태_수정_Y_to_N() {
        Member member = Member.builder()
                .email("email")
                .credit(100L)
                .attachmentFileName("123")
                .attachmentFileSize(100L)
                .attachmentOriginalFileName("123")
                .filePath("123")
                .nickname("1")
                .password("123")
                .createDate(LocalDateTime.now())
                .role(Role.USER)
                .build();
        memberJpaRepository.save(member);

        InterviewQuestion interviewQuestion = InterviewQuestion.builder()
                .interviewShow("Y")
                .createDate(LocalDateTime.now())
                .member(member)
                .question("test용 질문입니디.")
                .build();
        interviewQuestionJpaRepository.save(interviewQuestion);
        Long questionId = interviewQuestion.getId();

        interviewQuestionService.updateQuestionState(questionId);

        assertEquals("N", interviewQuestion.getInterviewShow());
    }

    @Test
    @DisplayName("없는 질문 수정 시 에러")
    public void 없는_질문_수정_예외() {
        Long questionId = 1L;

        Throwable exception = assertThrowsExactly(
                CustomException.class,
                () -> interviewQuestionService.updateQuestionState(questionId)
        );

        assertEquals(INVALID_QUESTION.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("질문 삭제")
    public void 질문_삭제() {
        Member member = Member.builder()
                .email("email")
                .credit(100L)
                .attachmentFileName("123")
                .attachmentFileSize(100L)
                .attachmentOriginalFileName("123")
                .filePath("123")
                .nickname("1")
                .password("123")
                .createDate(LocalDateTime.now())
                .role(Role.USER)
                .build();
        memberJpaRepository.save(member);

        InterviewQuestion interviewQuestion = InterviewQuestion.builder()
                .interviewShow("Y")
                .createDate(LocalDateTime.now())
                .member(member)
                .question("test용 질문입니디.")
                .build();
        interviewQuestionJpaRepository.save(interviewQuestion);
        Long questionId = interviewQuestion.getId();

        interviewQuestionService.deleteQuestion(questionId);

        Boolean exists = interviewQuestionJpaRepository.existsById(questionId);
        assertEquals(false, exists);
    }


    @Test
    @DisplayName("질문 스택 변경")
    public void 질문_스택_변경() {
        Member member = Member.builder()
                .email("email")
                .credit(100L)
                .attachmentFileName("123")
                .attachmentFileSize(100L)
                .attachmentOriginalFileName("123")
                .filePath("123")
                .nickname("1")
                .password("123")
                .createDate(LocalDateTime.now())
                .role(Role.USER)
                .build();
        memberJpaRepository.save(member);

        QuestionStack originStack = QuestionStack.builder()
                .price(0L)
                .stackName("자바")
                .description("자바칩 프라푸치노")
                .build();
        questionStackJpaRepository.save(originStack);

        InterviewQuestion interviewQuestion = InterviewQuestion.builder()
                .interviewShow("Y")
                .createDate(LocalDateTime.now())
                .member(member)
                .question("test용 질문입니디.")
                .questionStack(originStack)
                .build();
        interviewQuestionJpaRepository.save(interviewQuestion);

        QuestionStack stack = QuestionStack.builder()
                .price(0L)
                .stackName("리엑트")
                .description("Re-act")
                .build();
        questionStackJpaRepository.save(stack);

        Long questionId = interviewQuestion.getId();
        Long stackId = stack.getId();

        interviewQuestionService.updateQuestionStack(questionId, stackId);

        assertEquals(interviewQuestion.getQuestionStack(), stack);
    }

    @Test
    @DisplayName("질문 스택 등록")
    public void 질문_스택_등록() {
        Member member = Member.builder()
                .email("email")
                .credit(100L)
                .attachmentFileName("123")
                .attachmentFileSize(100L)
                .attachmentOriginalFileName("123")
                .filePath("123")
                .nickname("1")
                .password("123")
                .createDate(LocalDateTime.now())
                .role(Role.USER)
                .build();
        memberJpaRepository.save(member);

        InterviewQuestion interviewQuestion = InterviewQuestion.builder()
                .interviewShow("Y")
                .createDate(LocalDateTime.now())
                .member(member)
                .question("test용 질문입니디.")
                .build();
        interviewQuestionJpaRepository.save(interviewQuestion);

        QuestionStack stack = QuestionStack.builder()
                .price(0L)
                .stackName("자바")
                .description("자바칩 프라푸치노")
                .build();
        questionStackJpaRepository.save(stack);

        Long questionId = interviewQuestion.getId();
        Long stackId = stack.getId();

        interviewQuestionService.updateQuestionStack(questionId, stackId);

        assertEquals(interviewQuestion.getQuestionStack(), stack);
    }

    @Test
    @DisplayName("없는 질문 스택 등록")
    public void 없는_질문_스택_등록() {
        Member member = Member.builder()
                .email("email")
                .credit(100L)
                .attachmentFileName("123")
                .attachmentFileSize(100L)
                .attachmentOriginalFileName("123")
                .filePath("123")
                .nickname("1")
                .password("123")
                .createDate(LocalDateTime.now())
                .role(Role.USER)
                .build();
        memberJpaRepository.save(member);

        QuestionStack stack = QuestionStack.builder()
                .price(0L)
                .stackName("자바")
                .description("자바칩 프라푸치노")
                .build();
        questionStackJpaRepository.save(stack);

        Long questionId = 1L;
        Long stackId = stack.getId();

        Throwable exception = assertThrowsExactly(
                CustomException.class,
                () -> interviewQuestionService.updateQuestionStack(questionId, stackId)
        );

        assertEquals(INVALID_QUESTION.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("질문에 없는 스택 등록")
    public void 질문에_없는_스택_등록() {
        Member member = Member.builder()
                .email("email")
                .credit(100L)
                .attachmentFileName("123")
                .attachmentFileSize(100L)
                .attachmentOriginalFileName("123")
                .filePath("123")
                .nickname("1")
                .password("123")
                .createDate(LocalDateTime.now())
                .role(Role.USER)
                .build();
        memberJpaRepository.save(member);

        InterviewQuestion interviewQuestion = InterviewQuestion.builder()
                .interviewShow("Y")
                .createDate(LocalDateTime.now())
                .member(member)
                .question("test용 질문입니디.")
                .build();
        interviewQuestionJpaRepository.save(interviewQuestion);

        Long questionId = interviewQuestion.getId();
        Long stackId = 1L;

        Throwable exception = assertThrowsExactly(
                CustomException.class,
                () -> interviewQuestionService.updateQuestionStack(questionId, stackId)
        );

        assertEquals(INVALID_STACK.getMessage(), exception.getMessage());
    }
}