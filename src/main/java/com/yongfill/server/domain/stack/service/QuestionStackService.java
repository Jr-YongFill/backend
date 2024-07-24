package com.yongfill.server.domain.stack.service;

import com.yongfill.server.domain.stack.entity.QuestionStack;
import com.yongfill.server.domain.stack.repository.QuestionStackJpaRepository;
import com.yongfill.server.domain.stack.dto.QuestionStackDto;
import com.yongfill.server.global.common.response.error.ErrorCode;
import com.yongfill.server.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.yongfill.server.global.common.response.error.ErrorCode.INVALID_STACK;

@Service
@RequiredArgsConstructor
public class QuestionStackService {
    private final QuestionStackJpaRepository questionStackJpaRepository;

    @Transactional
    public QuestionStackDto.StackInsertResponseDto insertStack(QuestionStackDto.StackInsertRequestDto requestDto) {
        QuestionStack stack = requestDto.toEntity();
        questionStackJpaRepository.save(stack);

        return QuestionStackDto.StackInsertResponseDto.toDto(stack);
    }

    @Transactional
    public void updateStack(QuestionStackDto.StackUpdateRequestDto requestDto, Long stackId) {
        QuestionStack stack = questionStackJpaRepository.findById(stackId)
                .orElseThrow(() -> new CustomException(INVALID_STACK));

        stack.update(requestDto);
    }
}
