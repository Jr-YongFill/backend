package com.yongfill.server.domain.question.entity;

import com.yongfill.server.domain.member.entity.Member;
import com.yongfill.server.domain.stack.entity.QuestionStack;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Entity
public class InterviewQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "question", columnDefinition="text")
    private String question;

    @Column(name = "create_date")
    @CreatedDate
    private LocalDateTime createDate;

    @Column(name = "interview_show", length = 2)
    private String interviewShow;

    @JoinColumn(name = "question_stack_id")
    @ManyToOne
    private QuestionStack questionStack; ;

    @JoinColumn(name = "member_id")
    @ManyToOne
    private Member member;

}
