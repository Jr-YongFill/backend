package com.yongfill.server.domain.member.service;

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
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberJpaRepository memberJpaRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private static boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern).matcher(emailAddress).matches();
    }

    @Transactional
    public Member createMember(MemberRequestDTO memberRequestDTO) {
        String emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        if (!patternMatches(memberRequestDTO.getEmail(), emailPattern)) {
            throw new CustomException(ErrorCode.INVALID_EMAIL_FORMAT);
        }

        // 이메일 중복 확인
        Optional<Member> duplicateMember = memberJpaRepository.findMemberByEmail(memberRequestDTO.getEmail());
        if (duplicateMember.isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_MEMBER_EMAIL);
        }

        // 비밀번호 암호화
        String encryptedPassword = bCryptPasswordEncoder.encode(memberRequestDTO.getPassword());
        Member member = dtoToEntity(memberRequestDTO);
        member.setPassword(encryptedPassword);

        // 기본 파일 설정
        String defaultFileName = "default_profile_image.jpg";
        String defaultFilePath = "https://cdn.pixabay.com/photo/2016/09/21/18/16/companions-1685303_1280.jpg"; // 예시 URL
        member.setAttachmentFileName(defaultFileName);
        member.setAttachmentOriginalFileName("default_profile_image.jpg");
        member.setAttachmentFileSize(0L); // 기본 파일 크기
        member.setFilePath(defaultFilePath);

        return memberJpaRepository.save(member);
    }

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

        // 비밀번호 수정
        if (requestDTO.getPassword() != null && !requestDTO.getPassword().trim().isEmpty()) {
            member.setPassword(bCryptPasswordEncoder.encode(requestDTO.getPassword()));
        }

        // 닉네임 수정
        if (requestDTO.getNickname() != null && !requestDTO.getNickname().trim().isEmpty()) {
            Optional<Member> existingMemberWithNickname = memberJpaRepository.findMemberByNickname(requestDTO.getNickname());
            if (existingMemberWithNickname.isPresent() && !existingMemberWithNickname.get().getId().equals(memberId)) {
                throw new CustomException(ErrorCode.DUPLICATE_MEMBER_NICKNAME);
            }
            member.setNickname(requestDTO.getNickname());
        }

        // 파일 업데이트
        if (requestDTO.getFile() != null && !requestDTO.getFile().isEmpty()) {
            try {
                String fileName = UUID.randomUUID() + "_" + requestDTO.getFile().getOriginalFilename();
                String filePath = saveFile(requestDTO.getFile(), fileName);

                member.setAttachmentFileName(fileName);
                member.setAttachmentOriginalFileName(requestDTO.getFile().getOriginalFilename());
                member.setAttachmentFileSize(requestDTO.getFile().getSize());
                member.setFilePath(filePath);
            } catch (IOException e) {
                throw new CustomException(ErrorCode.PROFILE_SAVE_FAIL);
            }
        }

        return new MemberResponseDTO(memberJpaRepository.save(member));
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

    private String saveFile(MultipartFile file, String fileName) throws IOException {
        String uploadDir = "C://Users//user//uploads";
        File uploadDirectory = new File(uploadDir);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }

        File destinationFile = new File(uploadDirectory, fileName);
        file.transferTo(destinationFile);

        return destinationFile.getAbsolutePath();
    }

    private Member dtoToEntity(MemberRequestDTO requestDTO) {
        return Member.builder()
                .nickname(requestDTO.getNickname())
                .email(requestDTO.getEmail())
                .password(requestDTO.getPassword())
                .role(Role.USER)
                .createDate(LocalDateTime.now())
                .credit(190L)
                .attachmentFileSize(requestDTO.getAttachmentFileSize())
                .attachmentOriginalFileName(requestDTO.getAttachmentOriginalFileName())
                .attachmentFileName(requestDTO.getAttachmentFileName())
                .filePath(requestDTO.getFilePath())
                .build();
    }
}