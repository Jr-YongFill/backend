package com.yongfill.server.domain.posts.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.yongfill.server.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Entity
@Table(name = "post_like")
@Builder
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonBackReference
    @JoinColumn(name = "post_id", nullable = false)
    @ManyToOne
    private Post post;

    @JsonBackReference
    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne
    private Member member;
}
