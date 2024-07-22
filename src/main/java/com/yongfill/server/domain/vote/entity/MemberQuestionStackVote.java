package com.yongfill.server.domain.vote.entity;


import com.yongfill.server.domain.member.entity.Member;
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
public class MemberQuestionStackVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "question_stack_id")
    @ManyToOne
    private QuestionStack questionStack;

    @JoinColumn(name = "interview_question_id")
    @ManyToOne
    private InterviewQuestion interviewQuestion;

    @JoinColumn(name = "member_id")
    @ManyToOne
    private Member member;

}
