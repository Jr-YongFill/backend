package com.yongfill.server.domain.member.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Pattern;

import com.yongfill.server.domain.member.dto.MemberRequestDTO;
import com.yongfill.server.domain.member.dto.MemberResponseDTO;
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

    // member 회원조회
    @Transactional
    public MemberResponseDTO findMemberById(Long memberId) {
        Member member = memberJpaRepository.findMemberById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_MEMBER));
        return new MemberResponseDTO(member);
    }

    @Transactional
    public MemberResponseDTO updateMember(Long memberId, MemberRequestDTO requestDTO) {
        Member member = memberJpaRepository.findMemberById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_MEMBER));

        // 닉네임 중복 확인
        if (requestDTO.getNickname() != null) {
            Optional<Member> existingMemberByNickname = memberJpaRepository.findMemberByNickname(requestDTO.getNickname());
            if (existingMemberByNickname.isPresent() && !existingMemberByNickname.get().getId().equals(memberId)) {
                throw new IllegalArgumentException("닉네임 중복입니다.");
            }
            member.setNickname(requestDTO.getNickname());
        }

        // 비밀번호 변경
        if (requestDTO.getPassword() != null) {
            member.setPassword(bCryptPasswordEncoder.encode(requestDTO.getPassword()));
        }

        // 파일 경로 중복 확인
        if (requestDTO.getFilePath() != null) {
            Optional<Member> existingMemberByFilePath = memberJpaRepository.findMemberByFilePath(requestDTO.getFilePath());
            if (existingMemberByFilePath.isPresent() && !existingMemberByFilePath.get().getId().equals(memberId)) {
                throw new IllegalArgumentException("File path already in use.");
            }
            member.setFilePath(requestDTO.getFilePath());
        }

        // 파일 이름 중복 확인
        if (requestDTO.getAttachmentFileName() != null) {
            Optional<Member> existingMemberByFileName = memberJpaRepository.findMemberByAttachmentFileName(requestDTO.getAttachmentFileName());
            if (existingMemberByFileName.isPresent() && !existingMemberByFileName.get().getId().equals(memberId)) {
                throw new IllegalArgumentException("파일 이름 중복입니다.");
            }
            member.setAttachmentFileName(requestDTO.getAttachmentFileName());
        }

        // 파일 원본 이름 중복 확인
        if (requestDTO.getAttachmentOriginalFileName() != null) {
            member.setAttachmentOriginalFileName(requestDTO.getAttachmentOriginalFileName());
        }

        // 파일 크기 업데이트
        if (requestDTO.getAttachmentFileSize() != null) {
            member.setAttachmentFileSize(requestDTO.getAttachmentFileSize());
        }

        Member updatedMember = memberJpaRepository.save(member);
        return new MemberResponseDTO(updatedMember);
    }

    @Transactional
    public void deleteMember(Long memberId) {
        Member member = memberJpaRepository.findMemberById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_MEMBER));
        memberJpaRepository.delete(member);
    }

    @Transactional
    public Long getCreditByMemberId(Long memberId) {
        Member member = memberJpaRepository.findMemberById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_MEMBER));
        return member.getCredit();
    }

    private Member DtoToEntity(MemberRequestDTO requestDTO) {
        return Member.builder()
                .nickname(requestDTO.getNickname())
                .email(requestDTO.getEmail())
                .password(requestDTO.getPassword())
                .role(Role.USER) // 기본적으로 USER 역할 부여
                .createDate(LocalDateTime.now())
                .credit(190L)
                .attachmentFileSize(0L)
                .attachmentOriginalFileName("defaultOrifinalImageName")
                .attachmentFileName("defaultImageName")
                .filePath("https://cdn.pixabay.com/photo/2016/09/21/18/16/companions-1685303_1280.jpg")
                .build();
    }
}
