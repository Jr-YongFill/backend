package com.yongfill.server.domain.stack.service;

import com.yongfill.server.domain.stack.entity.QuestionStack;
import com.yongfill.server.domain.stack.repository.QuestionStackJpaRepository;
import com.yongfill.server.domain.stack.dto.QuestionStackDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
