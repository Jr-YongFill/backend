package com.yongfill.server.domain.vote.entity;


import com.yongfill.server.domain.member.entity.Member;
import com.yongfill.server.domain.question.entity.InterviewQuestion;
import com.yongfill.server.domain.stack.entity.QuestionStack;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Entity
@Table(name="member_question_stack_vote")
@Builder
public class MemberQuestionStackVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "question_stack_id", nullable = false)
    @ManyToOne
    private QuestionStack questionStack;

    @JoinColumn(name = "interview_question_id", nullable = false)
    @ManyToOne
    private InterviewQuestion interviewQuestion;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne
    private Member member;

}
