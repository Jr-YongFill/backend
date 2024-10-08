package com.yongfill.server.domain.question.entity;

import com.yongfill.server.domain.answer.entity.MemberAnswer;
import com.yongfill.server.domain.member.entity.Member;
import com.yongfill.server.domain.stack.entity.QuestionStack;
import com.yongfill.server.domain.vote.entity.CountVote;
import com.yongfill.server.domain.vote.entity.MemberQuestionStackVote;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString(exclude = {"member", "countVotes", "memberQuestionStackVotes", "memberAnswers"})
@Entity
@Table(name="interview_question")
@Builder
public class InterviewQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "question", columnDefinition="text", nullable = false)
    private String question;

    @Column(name = "create_date", nullable = false)
    @CreatedDate
    private LocalDateTime createDate;

    @Column(name = "interview_show", length = 2, nullable = false)
    private String interviewShow;

    @JoinColumn(name = "question_stack_id")
    @ManyToOne
    private QuestionStack questionStack; ;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne
    private Member member;

    @OneToMany(mappedBy = "interviewQuestion", orphanRemoval = true)
    private List<CountVote> countVotes;

    @OneToMany(mappedBy = "interviewQuestion", orphanRemoval = true)
    private List<MemberQuestionStackVote> memberQuestionStackVotes;

    @OneToMany(mappedBy = "interviewQuestion", orphanRemoval = true)
    private List<MemberAnswer> memberAnswers;

    public void updateInterviewShow() {
        this.interviewShow = getNextInterviewShowType();
    }

    private String getNextInterviewShowType() {
        if ("N".equals(this.interviewShow)) {
            return "Y";
        }

        return "N";
    }

    public void updateQuestionStack(QuestionStack stack) {
        this.questionStack = stack;
    }


}
