package com.yongfill.server.domain.vote.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yongfill.server.domain.question.entity.InterviewQuestion;
import com.yongfill.server.domain.stack.entity.QuestionStack;
import com.yongfill.server.domain.vote.entity.CountVote;
import com.yongfill.server.domain.vote.entity.MemberQuestionStackVote;
import com.yongfill.server.domain.vote.entity.QCountVote;
import com.yongfill.server.domain.vote.entity.QMemberQuestionStackVote;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class CountVoteQueryDSLRepository extends QuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;
    private final QCountVote qCountVote = QCountVote.countVote;

    public CountVoteQueryDSLRepository(JPAQueryFactory jpaQueryFactory) {
        super(MemberQuestionStackVote.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public CountVote findByQuestionAndStack(InterviewQuestion question, QuestionStack stack) {
        return jpaQueryFactory
                .selectFrom(qCountVote)
                .where(qCountVote.interviewQuestion.eq(question)
                        .and(qCountVote.questionStack.eq(stack)))
                .fetchOne();
    }
}
