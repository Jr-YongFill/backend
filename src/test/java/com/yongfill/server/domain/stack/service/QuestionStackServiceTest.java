package com.yongfill.server.domain.stack.service;

import com.yongfill.server.domain.stack.dto.QuestionStackDto;
import com.yongfill.server.domain.stack.entity.QuestionStack;
import com.yongfill.server.domain.stack.repository.QuestionStackJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

}