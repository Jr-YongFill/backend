package com.yongfill.server.domain.comments.entity;

import com.yongfill.server.domain.member.entity.Member;
import com.yongfill.server.domain.posts.entity.Post;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString (exclude = {"member", "post"})
@Entity
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Comment {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "content", length = 1000, nullable = false)
    private String content;

    @Column(name = "create_date", nullable = false)
    @CreatedDate
    private LocalDateTime createDate;

    @Column(name = "update_date", nullable = false)
    @LastModifiedDate
    private LocalDateTime updateDate;


    @Column(name = "update_yn", length = 2, nullable = false)
    private String updateYn;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne
    private Member member;

    @JoinColumn(name = "post_id", nullable = false)
    @ManyToOne
    private Post post;

    public void update(String content) {

        if (content != null) this.content = content;
        this.updateYn = "Y";
        this.updateDate = LocalDateTime.now();

    }

}
