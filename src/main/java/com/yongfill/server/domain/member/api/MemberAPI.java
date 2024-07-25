package com.yongfill.server.domain.member.api;

import com.yongfill.server.domain.member.dto.MemberDTO;
import com.yongfill.server.domain.member.entity.Member;
import com.yongfill.server.domain.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class MemberAPI {

    private final MemberService memberService;

    // 회원가입 API
    @PostMapping("/api/auth/sign-up")
    public ResponseEntity<Member> createMember(@RequestBody MemberDTO.MemberRequestDTO requestDTO) {
        HttpStatus status = HttpStatus.OK;
        Member member = memberService.createMember(requestDTO);
        return new ResponseEntity<>(member, status);
    }

    // 로그인 API
    @PostMapping("/api/auth/sign-in")
    public ResponseEntity<Boolean> findMemberLogin(@RequestBody MemberDTO.MemberRequestDTO requestDTO, HttpServletResponse response, HttpServletRequest request) {
        HttpStatus status = HttpStatus.OK;
        boolean loginSuccess = memberService.findMemberLogin(requestDTO.getEmail(), requestDTO.getPassword());
        if (!loginSuccess) {
            return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED); // 로그인 실패 시 401 응답
        }

        return new ResponseEntity<>(true, status); // 로그인 성공 시 200 응답
    }

    // 로그아웃 API
    @PostMapping("/api/logout")
    public ResponseEntity<Boolean> findUserLogout(HttpServletRequest request, HttpServletResponse response) {
        HttpStatus status = HttpStatus.OK;
        // 로그아웃 처리 로직 추가
        return new ResponseEntity<>(status);
    }

    // 사용자 목록 조회 API
    @PostMapping("/api/userFind")
    public ResponseEntity<List<MemberDTO.MemberResponseDTO>> findMember(MemberDTO.MemberRequestDTO memberRequestDTO) {
        HttpStatus status = HttpStatus.OK;
        List<MemberDTO.MemberResponseDTO> responseDTO = memberService.findMember();
        return new ResponseEntity<>(responseDTO, status);
    }
}
