package com.yongfill.server.domain.stack.service;

import com.yongfill.server.domain.member.entity.Member;
import com.yongfill.server.domain.member.entity.Role;
import com.yongfill.server.domain.member.repository.MemberJpaRepository;
import com.yongfill.server.domain.stack.dto.MemberStackAuthDto;
import com.yongfill.server.domain.stack.dto.QuestionStackDto;
import com.yongfill.server.domain.stack.entity.MemberStackAuth;
import com.yongfill.server.domain.stack.entity.QuestionStack;
import com.yongfill.server.domain.stack.repository.MemberStackAuthJpaRepository;
import com.yongfill.server.domain.stack.repository.QuestionStackJpaRepository;
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
class MemberStackAuthServiceTest {
    @Autowired
    MemberJpaRepository memberJpaRepository;
    @Autowired
    QuestionStackJpaRepository questionStackJpaRepository;
    @Autowired
    MemberStackAuthService memberStackAuthService;
    @Autowired
    MemberStackAuthJpaRepository memberStackAuthJpaRepository;

    @Test
    @DisplayName("스택 구입 테스트")
    void 스택_구입_테스트() {
        Member member = createMember("사람", Role.USER, 190L);
        QuestionStack stack = createStack("자바", 100L);
        Long memberId = member.getId();
        Long stackId = stack.getId();

        MemberStackAuthDto.AuthResponseDto responseDto = memberStackAuthService.purchaseStack(memberId, stackId);

        MemberStackAuth auth = memberStackAuthJpaRepository.findById(responseDto.getAuthId())
                .orElseThrow();

        assertEquals(auth.getId(), responseDto.getAuthId());
        assertEquals(190L - stack.getPrice(), member.getCredit());
    }

    @Test
    @DisplayName("스택 구입 테스트(돈 없음)")
    void 스택_구입_테스트_돈_없음() {
        Member member = createMember("사람", Role.USER, 0L);
        QuestionStack stack = createStack("자바", 100L);
        Long memberId = member.getId();
        Long stackId = stack.getId();

        Throwable exception = assertThrowsExactly(
                CustomException.class,
                () -> memberStackAuthService.purchaseStack(memberId, stackId)
        );

        assertEquals(MEMBER_CREDIT_NOT_ENOUGH.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("스택 구입 테스트(스택 없음)")
    void 스택_구입_테스트_스택_없음() {
        Member member = createMember("사람", Role.USER, 0L);
        Long memberId = member.getId();
        Long stackId = 1L;

        Throwable exception = assertThrowsExactly(
                CustomException.class,
                () -> memberStackAuthService.purchaseStack(memberId, stackId)
        );

        assertEquals(INVALID_STACK.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("스택 구입 테스트(멤버 없음)")
    void 스택_구입_테스트_멤버_없음() {
        QuestionStack stack = createStack("자바", 100L);
        Long memberId = 1L;
        Long stackId = stack.getId();

        Throwable exception = assertThrowsExactly(
                CustomException.class,
                () -> memberStackAuthService.purchaseStack(memberId, stackId)
        );

        assertEquals(INVALID_MEMBER.getMessage(), exception.getMessage());
    }


    @Test
    @DisplayName("스택 목록 조회")
    void 스택_목록_조회() {
        Member member = createMember("사람", Role.USER, 10000L);
        List<QuestionStack> stacks = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            QuestionStack stack = createStack(String.valueOf(i), 100L);
            if (i % 2 == 0) {
                MemberStackAuth memberStackAuth = MemberStackAuth.builder()
                        .member(member)
                        .questionStack(stack)
                        .build();
                memberStackAuthJpaRepository.save(memberStackAuth);
            }
        }
        Long memberId = member.getId();

        List<QuestionStackDto.StackResponseDto> stackInfos = memberStackAuthService.getStackInfo(memberId);

        assertEquals(10, stackInfos.size());
        assertEquals(5, stackInfos.stream()
                .filter(QuestionStackDto.StackResponseDto::getIsPurchase)
                .count());
    }

    private Member createMember(String name, Role role, Long credit) {
        Member member = Member.builder()
                .email(name)
                .credit(credit)
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

    private QuestionStack createStack(String name, Long credit) {
        QuestionStack stack = QuestionStack.builder()
                .price(credit)
                .stackName(name)
                .description(name)
                .build();
        questionStackJpaRepository.save(stack);

        return stack;
    }
}