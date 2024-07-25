package com.yongfill.server.domain.stack.service;

import com.yongfill.server.domain.member.entity.Member;
import com.yongfill.server.domain.member.repository.MemberJpaRepository;
import com.yongfill.server.domain.stack.dto.MemberStackAuthDto;
import com.yongfill.server.domain.stack.dto.QuestionStackDto;
import com.yongfill.server.domain.stack.entity.MemberStackAuth;
import com.yongfill.server.domain.stack.entity.QuestionStack;
import com.yongfill.server.domain.stack.repository.MemberStackAuthJpaRepository;
import com.yongfill.server.domain.stack.repository.QuestionStackJpaRepository;
import com.yongfill.server.domain.stack.repository.QuestionStackQueryDSLRepository;
import com.yongfill.server.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.yongfill.server.global.common.response.error.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class MemberStackAuthService {
    private final MemberJpaRepository memberJpaRepository;
    private final MemberStackAuthJpaRepository memberStackAuthJpaRepository;
    private final QuestionStackJpaRepository questionStackJpaRepository;
    private final QuestionStackQueryDSLRepository questionStackQueryDSLRepository;

    @Transactional
    public MemberStackAuthDto.AuthResponseDto purchaseStack(Long memberId, Long stackId) {
        Member member = memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(INVALID_MEMBER));

        QuestionStack stack = questionStackJpaRepository.findById(stackId)
                .orElseThrow(() -> new CustomException(INVALID_STACK));

        if (!member.isPurchaseStack(stack.getPrice())) {
            throw new CustomException(MEMBER_CREDIT_NOT_ENOUGH);
        }

        member.purchaseStack(stack.getPrice());

        MemberStackAuth auth = MemberStackAuth.builder()
                .questionStack(stack)
                .member(member)
                .build();

        memberStackAuthJpaRepository.save(auth);
        return MemberStackAuthDto.AuthResponseDto.toDto(auth);
    }

    @Transactional
    public List<QuestionStackDto.StackResponseDto> getStackInfo(Long memberId) {
        return questionStackQueryDSLRepository.findByMember(memberId);
    }
}
