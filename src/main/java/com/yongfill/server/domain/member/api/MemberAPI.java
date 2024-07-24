package com.yongfill.server.domain.member.api;

import java.util.List;

import com.yongfill.server.domain.member.dto.MemberDTO;
import com.yongfill.server.domain.member.dto.MemberLoginDTO;
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


@RequiredArgsConstructor
@Controller
public class MemberAPI {

    private final MemberService memberService;

    // 유저 회원가입
    @PostMapping("/api/auth/sign-up")
    public ResponseEntity<Member> createMember(@RequestBody MemberDTO.MemberRequestDTO requestDTO) {
        HttpStatus status = HttpStatus.CREATED;
        Member member = memberService.createMember(requestDTO);
        System.out.println(member);
        return new ResponseEntity<>(member, status);
    }

    // 로그인
    @PostMapping("/api/auth/sign-in")
    public ResponseEntity<Boolean> findMemberLogin(@RequestBody MemberDTO.MemberRequestDTO requestDTO, HttpServletResponse response, HttpServletRequest request) throws Exception {
        HttpStatus status = HttpStatus.OK;
        boolean loginSuccess = memberService.findMemberLogin(requestDTO.getEmail(), requestDTO.getPassword());
        System.out.println(loginSuccess);
//        Member cookiemember = memberService.getByEmail(requestDTO.getEmail());
//        MemberLoginDTO memberLoginDTO = new MemberLoginDTO(cookieMember);
//        request.setAttribute("memberLoginDTO", memberLoginDTO);	// 인터셉터에 userLoginDTO 사용

        return new ResponseEntity<>(status);

    }

    // 로그아웃
    @PostMapping("/api/logout")
    public ResponseEntity<Boolean> findUserLogout(HttpServletRequest request, HttpServletResponse response) {
        HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(status);
    }

    // UserList
    @PostMapping("/api/userFind")
    public ResponseEntity<List<MemberDTO.MemberResponseDTO>> findMember(MemberDTO.MemberRequestDTO memberRequestDTO) {
        HttpStatus status = HttpStatus.OK;
        List<MemberDTO.MemberResponseDTO> responseDTO = memberService.findMember();
        return new ResponseEntity<>(responseDTO, status);
    }

}
