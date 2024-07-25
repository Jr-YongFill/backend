package com.yongfill.server.domain.question.repository;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.QueryHandler;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.sql.JPASQLQuery;
import com.querydsl.sql.SQLTemplates;
import com.yongfill.server.domain.member.entity.QMember;
import com.yongfill.server.domain.question.dto.InterviewQuestionDto;
import com.yongfill.server.domain.question.dto.QInterviewQuestionDto_QuestionVoteResponseDto_QuestionPageDto;
import com.yongfill.server.domain.question.dto.QInterviewQuestionDto_QuestionVoteResponseDto_QuestionPageDto_StackDto;
import com.yongfill.server.domain.question.entity.InterviewQuestion;
import com.yongfill.server.domain.question.entity.QInterviewQuestion;
import com.yongfill.server.domain.stack.entity.QuestionStack;
import com.yongfill.server.domain.vote.entity.QCountVote;
import com.yongfill.server.domain.vote.entity.QMemberQuestionStackVote;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InterviewQuestionQueryDSLRepository extends QuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;
    private final QInterviewQuestion qInterviewQuestion = QInterviewQuestion.interviewQuestion;
    private final QMemberQuestionStackVote qMemberQuestionStackVote = QMemberQuestionStackVote.memberQuestionStackVote;
    private final QMember qMember = QMember.member;
    private final QCountVote qCountVote = QCountVote.countVote;
    private final com.querydsl.sql.Configuration configuration;
    private final QueryHandler queryHandler;

    @PersistenceContext
    private EntityManager em;

    public InterviewQuestionQueryDSLRepository(JPAQueryFactory jpaQueryFactory, com.querydsl.sql.Configuration configuration, QueryHandler queryHandler) {
        super(InterviewQuestion.class);
        this.jpaQueryFactory = jpaQueryFactory;
        this.configuration = configuration;
        this.queryHandler = queryHandler;
    }

    public List<InterviewQuestion> findQuestionRandomByStacks(List<QuestionStack> stacks, Long size) {
        return jpaQueryFactory
                .selectFrom(qInterviewQuestion)
                .where(qInterviewQuestion.inStacks(stacks),
                        qInterviewQuestion.isShow())
                .orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc())
                .limit(size)
                .fetch();
    }


    public Page<InterviewQuestionDto.QuestionVoteResponseDto.QuestionPageDto> getVoteQuestionInfo(Long memberId, Pageable pageable) {
        JPASQLQuery<?> jpaSqlQuery = new JPASQLQuery<>(em, configuration, queryHandler);
        StringPath mqsv = Expressions.stringPath("mqsv");
        StringPath iq = Expressions.stringPath("iq");
        NumberPath<Long> voteMemberId = Expressions.numberPath(Long.class, qMemberQuestionStackVote, "member_id");
        NumberPath<Long> voteQuestionId = Expressions.numberPath(Long.class, qMemberQuestionStackVote, "interview_question_id");
        NumberPath<Long> voteStackId = Expressions.numberPath(Long.class, qMemberQuestionStackVote, "question_stack_id");
        NumberPath<Long> qId = Expressions.numberPath(Long.class, qInterviewQuestion, "id");
        NumberPath<Long> iqId = Expressions.numberPath(Long.class, iq, "id");
        StringPath qQuestoin = Expressions.stringPath(qInterviewQuestion, "question");
        StringPath qMemberId = Expressions.stringPath(qInterviewQuestion, "member_id");

        List<InterviewQuestionDto.QuestionVoteResponseDto.QuestionPageDto> result =
                jpaSqlQuery
                        .from(JPAExpressions
                                .select(qId, qQuestoin, qMemberId)
                                .from(qInterviewQuestion)
                                .where(Expressions.numberPath(Long.class, qInterviewQuestion, "question_stack_id").isNull())
                                .orderBy(qInterviewQuestion.createDate.desc())
                                .limit(pageable.getPageSize())
                                .offset(pageable.getOffset()), iq)
                        .join(qMember)
                        .on(qMember.id.eq(
                                Expressions.numberPath(Long.class, iq, "member_id")))
                        .join(qCountVote)
                        .on(iqId.eq(
                                Expressions.numberPath(Long.class, qCountVote, "interview_question_id")))
                        .leftJoin(JPAExpressions
                                        .select(voteMemberId, voteQuestionId, voteStackId)
                                        .from(qMemberQuestionStackVote)
                                        .where(voteMemberId.eq(memberId))
                                , mqsv)
                        .on(iqId.eq(
                                Expressions.numberPath(Long.class, mqsv, "interview_question_id")
                        ))
                        .transform(GroupBy.groupBy(iqId).list(
                                new QInterviewQuestionDto_QuestionVoteResponseDto_QuestionPageDto(
                                        iqId,
                                        Expressions.stringPath(iq, "question"),
                                        qMember.nickname,
                                        Expressions.numberPath(Long.class, mqsv, "question_stack_id").coalesce(0L),
                                        GroupBy.list(
                                            new QInterviewQuestionDto_QuestionVoteResponseDto_QuestionPageDto_StackDto(
                                                    Expressions.numberPath(Long.class, qCountVote, "question_stack_id"),
                                                    Expressions.numberPath(Long.class, qCountVote, "count_")
                                                    )
                                        )
                                )
                        ));

        Long count = jpaQueryFactory.selectFrom(qInterviewQuestion)
                .where(qInterviewQuestion.questionStack.isNull())
                .stream().count();

        return new PageImpl<>(result, pageable, count);
    }
}
