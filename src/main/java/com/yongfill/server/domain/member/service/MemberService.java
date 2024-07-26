package com.yongfill.server.domain.member.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Pattern;

import com.yongfill.server.domain.member.dto.MemberRequestDTO;
import com.yongfill.server.domain.member.entity.Member;
import com.yongfill.server.domain.member.entity.Role;
import com.yongfill.server.domain.member.repository.MemberJpaRepository;
import com.yongfill.server.global.common.response.error.ErrorCode;
import com.yongfill.server.global.exception.CustomException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberJpaRepository memberJpaRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private static boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }

    @Transactional
    public Member createMember(MemberRequestDTO memberRequestDTO) {
        String emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        if (!patternMatches(memberRequestDTO.getEmail(), emailPattern)) {
            throw new CustomException(ErrorCode.INVALID_EMAIL_FORMAT);
        }

        // 이메일인지 있는지 확인
        Optional<Member> duplicateMember = memberJpaRepository.findMemberByEmail(memberRequestDTO.getEmail());
        if (duplicateMember.isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_MEMBER_EMAIL);
        }

        // 비밀번호 암호화
        String encryptedPassword = bCryptPasswordEncoder.encode(memberRequestDTO.getPassword());
        Member member = DtoToEntity(memberRequestDTO);
        member.setPassword(encryptedPassword);
        Member newMember = memberJpaRepository.save(member);

        return newMember;
    }

    private Member DtoToEntity(MemberRequestDTO requestDTO) {
        return Member.builder()
                .nickname(requestDTO.getNickname())
                .email(requestDTO.getEmail())
                .password(requestDTO.getPassword())
                .role(Role.USER) // 기본적으로 USER 역할 부여
                .createDate(LocalDateTime.now())
                .credit(190L)
//                .refreshToken("ass") // refreshToken 추가
                .attachmentFileSize(0L)
                .attachmentFileName("attachmentFileName")
                .filePath("filePath")
                .attachmentOriginalFileName("attachmentOriginalFileName")
                .build();
    }


}
