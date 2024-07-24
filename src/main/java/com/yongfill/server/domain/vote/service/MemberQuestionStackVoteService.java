package com.yongfill.server.domain.vote.service;

import com.yongfill.server.domain.member.entity.Member;
import com.yongfill.server.domain.member.repository.MemberJpaRepository;
import com.yongfill.server.domain.question.entity.InterviewQuestion;
import com.yongfill.server.domain.question.repository.InterviewQuestionJpaRepository;
import com.yongfill.server.domain.stack.entity.QuestionStack;
import com.yongfill.server.domain.stack.repository.QuestionStackJpaRepository;
import com.yongfill.server.domain.vote.entity.CountVote;
import com.yongfill.server.domain.vote.entity.MemberQuestionStackVote;
import com.yongfill.server.domain.vote.repository.CountVoteQueryDSLRepository;
import com.yongfill.server.domain.vote.repository.MemberQuestionStackVoteJpaRepository;
import com.yongfill.server.domain.vote.repository.MemberQuestionStackVoteQueryDSLRepository;
import com.yongfill.server.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.yongfill.server.global.common.response.error.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class MemberQuestionStackVoteService {
    private final MemberQuestionStackVoteJpaRepository memberQuestionStackVoteJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final QuestionStackJpaRepository questionStackJpaRepository;
    private final InterviewQuestionJpaRepository interviewQuestionJpaRepository;
    private final MemberQuestionStackVoteQueryDSLRepository memberQuestionStackVoteQueryDSLRepository;
    private final CountVoteQueryDSLRepository countVoteQueryDSLRepository;

    @Transactional
    public void vote(Long memberId, Long stackId, Long questionId) {
        Member member = memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(INVALID_MEMBER));
        QuestionStack stack = questionStackJpaRepository.findById(stackId)
                .orElseThrow(() -> new CustomException(INVALID_STACK));
        InterviewQuestion question = interviewQuestionJpaRepository.findById(questionId)
                .orElseThrow(() -> new CustomException(INVALID_QUESTION));

        if (memberQuestionStackVoteQueryDSLRepository.isVote(member, stack, question)) {
            throw new CustomException(MEMBER_ALREADY_VOTE);
        }

        MemberQuestionStackVote memberQuestionStackVote = MemberQuestionStackVote.builder()
                .member(member)
                .questionStack(stack)
                .interviewQuestion(question)
                .build();

        memberQuestionStackVoteJpaRepository.save(memberQuestionStackVote);

        CountVote countVote = countVoteQueryDSLRepository.findByQuestionAndStack(question, stack);
        countVote.plusCount();
    }
}
