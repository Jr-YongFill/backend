package com.yongfill.server.domain.posts.entity;
import com.yongfill.server.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Entity
@Builder
public class View {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "post_id", nullable = false)
    @ManyToOne
    private Post post;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne
    private Member member;

}
