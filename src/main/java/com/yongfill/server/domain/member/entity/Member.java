package com.yongfill.server.domain.member.entity;

import com.yongfill.server.domain.answer.entity.MemberAnswer;
import com.yongfill.server.domain.comments.entity.Comment;
import com.yongfill.server.domain.posts.entity.Post;
import com.yongfill.server.domain.question.entity.InterviewQuestion;
import com.yongfill.server.domain.stack.entity.MemberStackAuth;
import com.yongfill.server.domain.vote.entity.MemberQuestionStackVote;
import lombok.*;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString(exclude = {"posts", "comments", "stacks", "votes", "questions", "answers"})
@Entity
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", length = 200, nullable = false)
    private String email;

    @Column(name = "password", length = 300, nullable = false)
    private String password;

    @Column(name = "credit", nullable = false)
    private Long credit;

    @Column(name = "nickname", length = 50, nullable = false)
    private String nickname;

    @Column(name = "create_date", nullable = false)
    @CreatedDate
    private LocalDateTime createDate;

    @Column(name = "role", length = 10, nullable = false)
    private Role role;

    @Column(name = "file_path", length = 2000, nullable = false)
    private String filePath;

    @Column(name = "attachment_file_name", length = 2000, nullable = false)
    private String attachmentFileName;

    @Column(name = "attachment_original_file_name", length = 2000, nullable = false)
    private String attachmentOriginalFileName;

    @Column(name = "attachment_file_size", nullable = false)
    private Long attachmentFileSize;

    @Column(name = "refresh_token", length = 1000)
    private String refreshToken;

    @OneToMany(mappedBy = "member", orphanRemoval = true)
    private List<Post> posts;

    @OneToMany(mappedBy = "member", orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "member", orphanRemoval = true)
    private List<MemberStackAuth> stacks;

    @OneToMany(mappedBy = "member", orphanRemoval = true)
    private List<MemberQuestionStackVote> votes;

    @OneToMany(mappedBy = "member", orphanRemoval = true)
    private List<InterviewQuestion> questions;

    @OneToMany(mappedBy = "member", orphanRemoval = true)
    private List<MemberAnswer> answers;

    @Builder
    public Member(Long id, String email, String password, Long credit, String nickname, LocalDateTime createDate, Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.credit = credit;
        this.nickname = nickname;
        this.createDate = createDate;
        this.role = role;
    }

}