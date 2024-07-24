package com.yongfill.server.domain.stack.repository;

import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.sql.JPASQLQuery;
import com.querydsl.sql.SQLTemplates;
import com.yongfill.server.domain.member.entity.Member;
import com.yongfill.server.domain.stack.dto.QQuestionStackDto_StackResponseDto;
import com.yongfill.server.domain.stack.dto.QuestionStackDto;
import com.yongfill.server.domain.stack.entity.QMemberStackAuth;
import com.yongfill.server.domain.stack.entity.QQuestionStack;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QuestionStackQueryDSLRepository {
    private final SQLTemplates sqlTemplates;

    @PersistenceContext
    private EntityManager entityManager;

    public List<QuestionStackDto.StackResponseDto> findByMember(Long memberId) {
        JPASQLQuery<?> jpaSqlQuery = new JPASQLQuery<>(entityManager, sqlTemplates);
        QMemberStackAuth qMemberStackAuth = QMemberStackAuth.memberStackAuth;
        QQuestionStack qQuestionStack = QQuestionStack.questionStack;
        StringPath msa = Expressions.stringPath("msa");
        NumberPath<Long> memberStackAuthMemberId = Expressions.numberPath(Long.class, qMemberStackAuth, "member_id");
        NumberPath<Long> memberStackAuthStackId = Expressions.numberPath(Long.class, qMemberStackAuth, "question_stack_id");

        return jpaSqlQuery
                .select(new QQuestionStackDto_StackResponseDto(
                        qQuestionStack.id,
                        qQuestionStack.stackName,
                        qQuestionStack.price,
                        qQuestionStack.description,
                        new CaseBuilder()
                                .when(Expressions.numberPath(Long.class, msa, "member_id").isNotNull())
                                .then(true)
                                .otherwise(false)
                        )
                )
                .from(qQuestionStack)
                .leftJoin(JPAExpressions
                                .select(memberStackAuthMemberId, memberStackAuthStackId)
                                .from(qMemberStackAuth)
                                .where(memberStackAuthMemberId.eq(memberId))
                , msa)
                .on(qQuestionStack.id.eq(
                        Expressions.numberPath(Long.class, msa, "question_stack_id")
                ))
                .fetch();
    }
}
