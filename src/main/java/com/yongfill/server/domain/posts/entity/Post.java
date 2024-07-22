package com.yongfill.server.domain.posts.entity;

import com.yongfill.server.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Entity
public class Post {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", length = 200)
    private String title;

    @Column(name = "content", columnDefinition="longtext")
    private String content;

    @Column(name = "post_category", length = 30)
    private Category category;

    @Column(name = "create_date")
    @CreatedDate
    private LocalDateTime createDate;

    @Column(name = "update_date")
    @LastModifiedDate
    private LocalDateTime updateDate;

    @Column(name = "view_count")
    private Long viewCount;

    @Column(name = "like_count")
    private Long likeCount;

    @Column(name = "update_yn", length = 2)
    private String updateYn;

    @JoinColumn(name = "member_id")
    @ManyToOne
    private Member member;

}
