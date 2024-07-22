package com.yongfill.server.domain.answer.entity;

import com.yongfill.server.domain.member.entity.Member;
import com.yongfill.server.domain.question.entity.InterviewQuestion;
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
public class MemberAnswer {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "member_answer", columnDefinition="text", nullable = false)
    private String memberAnswer;

    @Column(name = "gpt_answer", columnDefinition="text", nullable = false)
    private String gptAnswer;

    @Column(name = "interview_mode", length = 2, nullable = false)
    private InterviewMode interviewMode;

    @Column(name = "create_date", nullable = false)
    @CreatedDate
    private LocalDateTime createDate;

    @JoinColumn(name = "interview_question_id", nullable = false)
    @ManyToOne
    private InterviewQuestion interviewQuestion;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne
    private Member member;



}
