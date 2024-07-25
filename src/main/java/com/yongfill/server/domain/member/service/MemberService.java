    package com.yongfill.server.domain.member.service;

    import java.time.LocalDateTime;
    import java.util.List;
    import java.util.Optional;
    import java.util.regex.Pattern;
    import java.util.stream.Collectors;

    import com.yongfill.server.domain.member.dto.MemberDTO;
    import com.yongfill.server.domain.member.entity.Member;
    import com.yongfill.server.domain.member.entity.Role;
    import com.yongfill.server.domain.member.repository.MemberJpaRepository;
    import com.yongfill.server.global.common.response.error.ErrorCode;
    import com.yongfill.server.global.exception.CustomException;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.annotation.Lazy;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    import lombok.RequiredArgsConstructor;

    @RequiredArgsConstructor
    @Service
    public class MemberService {


        @Autowired
        private MemberJpaRepository memberJpaRepository;

        @Autowired
        @Lazy
        private BCryptPasswordEncoder bCryptPasswordEncoder;


        private static boolean patternMatches(String emailAddress, String regexPattern) {
            return Pattern.compile(regexPattern)
                    .matcher(emailAddress)
                    .matches();
        }

        @Transactional
        public Member createMember(MemberDTO.MemberRequestDTO requestDTO) {
            // 이메일 패턴사용
            String emailPattern ="^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
            if (!patternMatches(requestDTO.getEmail(), emailPattern)) {
                throw new CustomException(ErrorCode.INVALID_EMAIL_FORMAT);
            }

            // 이메일인지 있는지 확인
            Optional<Member> duplicateMember = memberJpaRepository.findMemberByEmail(requestDTO.getEmail());
            if (duplicateMember.isPresent()) {
                throw new CustomException(ErrorCode.DUPLICATE_MEMBER_EMAIL);
            }

            // 비밀번호 암호화
            String encryptedPassword = bCryptPasswordEncoder.encode(requestDTO.getPassword());
            Member member = DtoToEntity(requestDTO);
            member.setPassword(encryptedPassword);
            Member newMember = memberJpaRepository.save(member);
            return newMember;
        }

        @Transactional
        public boolean findMemberLogin(String email, String password) {
            Optional<Member> member = memberJpaRepository.findMemberByEmail(email);	// 로그인을 하기위해 이메일 중복 확인

            if (member == null || !bCryptPasswordEncoder.matches(password, member.get().getPassword())) {
                throw new CustomException(ErrorCode.LOGIN_FAIL);
            }
            return true;
        }

        @Transactional
        public List<MemberDTO.MemberResponseDTO> findMember() {
            return memberJpaRepository		// Member에 등록된 회원 전부 가져오기
                    .findAll().stream()
                    .map(member -> new MemberDTO.MemberResponseDTO(
                            member.getId(), member.getNickname(), member.getEmail(), member.getPassword(), member.getRole(), member.getCreateDate(), member.getCredit()))
                    .collect(Collectors.toList());
        }


        public Member DtoToEntity(MemberDTO.MemberRequestDTO requestDTO) {
            Member member = Member.builder()			// Member 생성시 Role을 USER로 고정
                    .nickname(requestDTO.getNickname())
                    .email(requestDTO.getEmail())
                    .password(requestDTO.getPassword())
                    .credit(190L)
                    .role(Role.USER)
                    .createDate(LocalDateTime.now())
                    .filePath("filePath")
                    .attachmentFileName("attachmentFileName")
                    .attachmentOriginalFileName("attachmentOriginalFileName")
                    .attachmentFileSize(130L)
    //                .refreshToken("refreshToken")
                    .build();


            return member;
        }

    }
