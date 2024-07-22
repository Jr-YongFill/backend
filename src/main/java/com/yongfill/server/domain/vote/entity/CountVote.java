package com.yongfill.server.domain.vote.entity;

import com.yongfill.server.domain.question.entity.InterviewQuestion;
import com.yongfill.server.domain.stack.entity.QuestionStack;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Entity
public class CountVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "count")
    private Long count;

    @JoinColumn(name = "interview_question_id")
    @ManyToOne
    private InterviewQuestion interviewQuestion;

    @JoinColumn(name = "question_stack_id")
    @ManyToOne
    private QuestionStack questionStack;
}
