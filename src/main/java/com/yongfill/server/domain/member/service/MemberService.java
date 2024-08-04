package com.yongfill.server.domain.member.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.yongfill.server.domain.member.dto.MemberRequestDTO;
import com.yongfill.server.domain.member.dto.MemberResponseDTO;
import com.yongfill.server.domain.member.entity.Member;
import com.yongfill.server.domain.member.entity.Role;
import com.yongfill.server.domain.member.repository.MemberJpaRepository;
import com.yongfill.server.global.common.response.error.ErrorCode;
import com.yongfill.server.global.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

import javax.imageio.IIOException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class MemberService {

    private final MemberJpaRepository memberJpaRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AmazonS3 s3Client;
    private final String bucketName;
    private final String profileBucketUrl;
    private final String profileDirectoryName;

    public MemberService(MemberJpaRepository memberJpaRepository,
                         BCryptPasswordEncoder bCryptPasswordEncoder,
                         AmazonS3 s3Client,
                         @Value("${cloud.aws.s3.bucket}") String bucketName,
                         @Value("${cloud.aws.region.static}") String region,
                         @Value("${cloud.aws.directory.profile}") String profileDirectoryName) {
        this.memberJpaRepository = memberJpaRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.profileDirectoryName = profileDirectoryName;
        this.profileBucketUrl = "https://"+bucketName+".s3."+region+".amazonaws.com";
    }

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
        String defaultFileName = "default.jpg";
        String defaultFilePath = profileBucketUrl + "/" + profileDirectoryName + "/" + defaultFileName; // 환경변수에서 설정
        member.setAttachmentFileName(defaultFileName);
        member.setAttachmentOriginalFileName(defaultFileName);
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
                MultipartFile file = requestDTO.getFile();
                if (!"default.jpg".equals(member.getAttachmentOriginalFileName())) {
                    s3Client.deleteObject(bucketName, member.getAttachmentFileName());
                }

                String fileName = generateFileName(file, profileDirectoryName);
                s3Client.putObject(bucketName, fileName, file.getInputStream(), getObjectMetadata(file));

                member.setAttachmentFileName(fileName);
                member.setAttachmentFileSize(file.getSize());
                member.setAttachmentOriginalFileName(file.getOriginalFilename());
                member.setFilePath(profileBucketUrl + "/" + fileName);
            } catch (SdkClientException | IOException e) {
                throw new CustomException(ErrorCode.PROFILE_SAVE_FAIL);
            }
        }

        return new MemberResponseDTO(memberJpaRepository.save(member));
    }


    @Transactional
    public void deleteMember(Long memberId) {
        Member member = memberJpaRepository.findMemberById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_MEMBER));

        if(!"default.jpg".equals(member.getAttachmentOriginalFileName())) {
            s3Client.deleteObject(bucketName, member.getAttachmentFileName());
        }

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

    private String generateFileName(MultipartFile file, String mode) {
        return mode+"/"+UUID.randomUUID().toString()+ file.getOriginalFilename();
    }


    private ObjectMetadata getObjectMetadata(MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        return objectMetadata;
    }
}