package com.yongfill.server.domain.stack.entity;

import com.yongfill.server.domain.vote.entity.CountVote;
import com.yongfill.server.domain.vote.entity.MemberQuestionStackVote;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Entity
@Builder
public class QuestionStack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "stack_name", length = 100, nullable = false)
    private String stackName;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "description", length = 500, nullable = false)
    private String description;

    @OneToMany(mappedBy = "questionStack")
    private List<CountVote> countVotes;

    @OneToMany(mappedBy = "questionStack")
    private List<MemberQuestionStackVote> memberQuestionStackVotes;

    @OneToMany(mappedBy = "questionStack")
    private List<MemberStackAuth> memberStackAuths;
}
