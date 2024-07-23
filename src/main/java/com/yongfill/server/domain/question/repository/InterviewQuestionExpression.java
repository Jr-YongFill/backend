package com.yongfill.server.domain.question.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.annotations.QueryDelegate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.yongfill.server.domain.question.entity.InterviewQuestion;
import com.yongfill.server.domain.question.entity.QInterviewQuestion;
import com.yongfill.server.domain.stack.entity.QuestionStack;

import java.util.List;

public class InterviewQuestionExpression {
    @QueryDelegate(InterviewQuestion.class)
    public static BooleanBuilder inStacks(QInterviewQuestion qInterviewQuestion, List<QuestionStack> stacks) {
        BooleanBuilder builder = new BooleanBuilder();
        stacks.forEach(stack -> builder.or(qInterviewQuestion.questionStack.eq(stack)));
        return builder;
    }

    @QueryDelegate(InterviewQuestion.class)
    public static BooleanExpression isShow(QInterviewQuestion qInterviewQuestion) {
        return qInterviewQuestion.interviewShow.eq("Y");
    }
}
