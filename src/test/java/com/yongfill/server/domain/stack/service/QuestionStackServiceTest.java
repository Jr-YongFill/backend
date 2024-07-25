package com.yongfill.server.domain.stack.service;

import com.yongfill.server.domain.member.entity.Member;
import com.yongfill.server.domain.member.entity.Role;
import com.yongfill.server.domain.question.entity.InterviewQuestion;
import com.yongfill.server.domain.stack.dto.QuestionStackDto;
import com.yongfill.server.domain.stack.entity.QuestionStack;
import com.yongfill.server.domain.stack.repository.QuestionStackJpaRepository;
import com.yongfill.server.global.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static com.yongfill.server.global.common.response.error.ErrorCode.INVALID_STACK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
class QuestionStackServiceTest {
    @Autowired
    QuestionStackService questionStackService;
    @Autowired
    QuestionStackJpaRepository questionStackJpaRepository;

    @Test
    @DisplayName("스택 생성 테스트")
    void 스택_생성_테스트() {
        QuestionStackDto.StackInsertRequestDto requestDto = QuestionStackDto.StackInsertRequestDto.builder()
                .stackName("자바")
                .price(100L)
                .description("자바칩 푸라푸치노")
                .build();

        QuestionStackDto.StackInsertResponseDto responseDto = questionStackService.insertStack(requestDto);

        QuestionStack questionStack = questionStackJpaRepository.findById(responseDto.getStackId())
                .orElseThrow();

        assertEquals(questionStack.getId(), responseDto.getStackId());
    }

    @Test
    @DisplayName("스택 수정 테스트")
    void 스택_수정_테스트() {
        QuestionStack stack = createStack("자바");
        QuestionStackDto.StackUpdateRequestDto requestDto = QuestionStackDto.StackUpdateRequestDto.builder()
                .stackName("국물")
                .price(100L)
                .description("자바칩 푸라푸치노")
                .build();
        Long stackId = stack.getId();

        questionStackService.updateStack(requestDto, stackId);

        assertEquals(stack.getStackName(), requestDto.getStackName());
        assertEquals(stack.getDescription(), requestDto.getDescription());
        assertEquals(stack.getPrice(), requestDto.getPrice());
    }

    @Test
    @DisplayName("없는 스택 수정")
    public void 없는_스택_수정() {
        QuestionStackDto.StackUpdateRequestDto requestDto = QuestionStackDto.StackUpdateRequestDto.builder()
                .stackName("국물")
                .price(100L)
                .description("자바칩 푸라푸치노")
                .build();
        Long stackId = 1L;

        Throwable exception = assertThrowsExactly(
                CustomException.class,
                () -> questionStackService.updateStack(requestDto, stackId)
        );

        assertEquals(INVALID_STACK.getMessage(), exception.getMessage());
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