package com.yongfill.server.domain.member.dto;

import com.yongfill.server.domain.member.entity.Member;
import com.yongfill.server.domain.member.entity.Role;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDTO {
    private Long id;
    private String nickname;
    private String email;
    private Role role;
    private LocalDateTime createDate;
    private Long credit;
    private String refreshToken;
    private String attachmentFileName;
    private String attachmentOriginalFileName;
    private Long attachmentFileSize;
    private String filePath;
    private String password;
    private MultipartFile file;


    public Member toEntity() {
        return Member.builder()
                .id(this.getId())
                .nickname(this.getNickname())
                .password(this.getPassword())
                .email(this.getEmail())
                .role(this.getRole())
                .createDate(this.getCreateDate())
                .credit(this.getCredit())
                .refreshToken(this.getRefreshToken())
                .attachmentFileName("aa")
                .attachmentOriginalFileName("dd")
                .attachmentFileSize(15L)
                .filePath("asd")
                .build();
    }
}