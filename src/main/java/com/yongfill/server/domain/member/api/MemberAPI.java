package com.yongfill.server.domain.member.api;

import com.yongfill.server.domain.auth.config.JwtTokenProvider;
import com.yongfill.server.domain.member.dto.MemberRequestDTO;
import com.yongfill.server.domain.member.dto.MemberResponseDTO;
import com.yongfill.server.domain.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
public class MemberAPI {

    private final MemberService memberService;

    // 회원가입 API
    @PostMapping("/sign-up")
    public ResponseEntity<Void> createMember(@RequestBody MemberRequestDTO requestDTO) {
        memberService.createMember(requestDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // 로그인 API
//    @PostMapping("/sign-in")
//    public ResponseEntity<?> findMember(@RequestBody MemberRequestDTO requestDTO) {
//        boolean loginSuccess = memberService.findMemberLogin(requestDTO.getEmail(), requestDTO.getPassword());
//        if (!loginSuccess) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();  // 401에러
//        }
//
//        // 로그인 성공 시 추가적인 로직이 필요한 경우에 대비하여 처리할 수 있음
//        return ResponseEntity.ok(loginSuccess);
//    }

//    @PostMapping("/sign-in")
//    public ResponseEntity<?> findMember(@RequestHeader("Authorization") String accessToken) {
//        Long id = jwtTokenProvider.getUserIdFromToken(accessToken.substring(7));
//        MemberResponseDTO memberResponseDto = memberService.findById(id);
//
//        // 로그인 성공 시 추가적인 로직이 필요한 경우에 대비하여 처리할 수 있음
//        return ResponseEntity.status(HttpStatus.OK).body(memberResponseDto);
//    }

    // 로그아웃 API
//    @PostMapping("/logout")
//    public ResponseEntity<?> findUserLogout(HttpServletRequest request, HttpServletResponse response) {
//        // 로그아웃 처리 로직 추가
//        // 세션을 무효화하거나 토큰을 만료시키는 등의 처리를 수행할 수 있음
//        return ResponseEntity.ok().build();
//    }

    // 사용자 목록 조회 API
//    @GetMapping("/users")
//    public ResponseEntity<List<MemberDTO.MemberResponseDTO>> findMembers() {
//        List<MemberDTO.MemberResponseDTO> responseDTOs = memberService.findAllMembers();
//        return ResponseEntity.ok(responseDTOs);
//    }
}
