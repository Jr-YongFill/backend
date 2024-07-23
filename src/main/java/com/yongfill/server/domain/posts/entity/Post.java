package com.yongfill.server.domain.posts.entity;

import com.yongfill.server.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString(exclude = {"likes", "views"})
@Entity
@Builder
public class Post {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @Column(name = "content", columnDefinition="longtext", nullable = false)
    private String content;

    @Column(name = "post_category", length = 30, nullable = false)
    private Category category;

    @Column(name = "create_date", nullable = false)
    @CreatedDate
    private LocalDateTime createDate;

    @Column(name = "update_date", nullable = false)
    @LastModifiedDate
    private LocalDateTime updateDate;

    @Column(name = "view_count", nullable = false)
    private Long viewCount;

    @Column(name = "like_count", nullable = false)
    private Long likeCount;

    @Column(name = "update_yn", length = 2, nullable = false)
    private String updateYn;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne
    private Member member;

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private List<Like> likes;

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private List<View> views;

}
