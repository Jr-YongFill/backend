package com.yongfill.server.domain.stack.entity;

import com.yongfill.server.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Entity
@Builder
public class MemberStackAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "question_stack_id", nullable = false)
    @ManyToOne
    private QuestionStack questionStack;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne
    private Member member;

}