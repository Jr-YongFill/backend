package com.yongfill.server.domain.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Entity
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


}