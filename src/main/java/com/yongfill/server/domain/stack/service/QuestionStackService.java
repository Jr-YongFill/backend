package com.yongfill.server.domain.stack.service;

import com.yongfill.server.domain.stack.entity.QuestionStack;
import com.yongfill.server.domain.stack.repository.QuestionStackJpaRepository;
import com.yongfill.server.domain.stack.dto.QuestionStackDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionStackService {
    private final QuestionStackJpaRepository questionStackJpaRepository;

    public QuestionStackDto.StackInsertResponseDto insertStack(QuestionStackDto.StackInsertRequestDto requestDto) {
        QuestionStack stack = requestDto.toEntity();
        questionStackJpaRepository.save(stack);

        return QuestionStackDto.StackInsertResponseDto.toDto(stack);
    }
}
