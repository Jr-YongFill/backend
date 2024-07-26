package com.yongfill.server.domain.posts.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.yongfill.server.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString(exclude = {"likes", "views"})
@Entity
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "longtext", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "post_category", length = 30, nullable = false)
    private Category category;

    @Column(name = "create_date", nullable = false)
    @CreatedDate
    private LocalDateTime createDate;

    @Column(name = "update_date", nullable = false)
    @LastModifiedDate
    private LocalDateTime updateDate;

    @ColumnDefault("0")
    @Column(name = "view_count", nullable = false)
    private Long viewCount;

    @ColumnDefault("0")
    @Column(name = "like_count", nullable = false)
    private Long likeCount;

    @ColumnDefault("'N'")
    @Column(name = "update_yn", length = 2, nullable = false)
    private String updateYn;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne
    private Member member;

    @JsonBackReference
    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private List<Like> likes;

    @JsonBackReference
    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private List<View> views;

    public void update(String title, Category category, String content) {
        if (title != null) this.title = title;
        if (category != null) this.category = category;
        if (content != null) this.content = content;
        this.updateYn = "Y";
        this.updateDate = LocalDateTime.now();
    }
}
