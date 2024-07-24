package com.yongfill.server.domain.vote.entity;

import com.yongfill.server.domain.question.entity.InterviewQuestion;
import com.yongfill.server.domain.stack.entity.QuestionStack;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Entity
@Builder
public class CountVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "count", nullable = false)
    private Long count;

    @JoinColumn(name = "interview_question_id", nullable = false)
    @ManyToOne
    private InterviewQuestion interviewQuestion;

    @JoinColumn(name = "question_stack_id", nullable = false)
    @ManyToOne
    private QuestionStack questionStack;

    public void plusCount() {
        this.count++;
    }
}
