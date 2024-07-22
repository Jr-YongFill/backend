package com.yongfill.server.domain.stack.entity;

import com.yongfill.server.domain.member.entity.Member;
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
public class MemberStackAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "question_stack_id")
    @ManyToOne
    private QuestionStack questionStack;

    @JoinColumn(name = "member_id")
    @ManyToOne
    private Member member;

}