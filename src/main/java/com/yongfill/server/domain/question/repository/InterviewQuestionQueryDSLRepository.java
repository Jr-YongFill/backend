package com.yongfill.server.domain.question.repository;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yongfill.server.domain.question.entity.InterviewQuestion;
import com.yongfill.server.domain.question.entity.QInterviewQuestion;
import com.yongfill.server.domain.stack.entity.QuestionStack;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InterviewQuestionQueryDSLRepository extends QuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;
    private final QInterviewQuestion qInterviewQuestion = QInterviewQuestion.interviewQuestion;

    public InterviewQuestionQueryDSLRepository(JPAQueryFactory jpaQueryFactory) {
        super(InterviewQuestion.class);
        this.jpaQueryFactory = jpaQueryFactory;
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
}
