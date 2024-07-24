package com.yongfill.server.domain.vote.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yongfill.server.domain.member.entity.Member;
import com.yongfill.server.domain.question.entity.InterviewQuestion;
import com.yongfill.server.domain.stack.entity.QuestionStack;
import com.yongfill.server.domain.vote.entity.MemberQuestionStackVote;
import com.yongfill.server.domain.vote.entity.QMemberQuestionStackVote;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class MemberQuestionStackVoteQueryDSLRepository extends QuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;
    private final QMemberQuestionStackVote qMemberQuestionStackVote = QMemberQuestionStackVote.memberQuestionStackVote;

    public MemberQuestionStackVoteQueryDSLRepository(JPAQueryFactory jpaQueryFactory) {
        super(MemberQuestionStackVote.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Boolean isVote(Member member, InterviewQuestion question) {
        return !jpaQueryFactory
                .selectFrom(qMemberQuestionStackVote)
                .where(qMemberQuestionStackVote.member.eq(member)
                        .and(qMemberQuestionStackVote.interviewQuestion.eq(question)))
                .fetch()
                .isEmpty();
    }
}
