package com.yongfill.server.domain.member.api;

import com.yongfill.server.domain.auth.config.JwtTokenProvider;
import com.yongfill.server.domain.member.dto.MemberRequestDTO;
import com.yongfill.server.domain.member.dto.MemberResponseDTO;
import com.yongfill.server.domain.member.service.MemberService;
import com.yongfill.server.domain.posts.dto.DeletePostDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class MemberAPI {

    private final MemberService memberService;

    // 회원가입
    @PostMapping("/sign-up")
    public ResponseEntity<?> createMember(@RequestBody MemberRequestDTO requestDTO) {
        HttpStatus status = HttpStatus.OK;
        memberService.createMember(requestDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // member 조회
    @GetMapping("/members/{member_id}")
    public ResponseEntity<?> myPage(@PathVariable("member_id") Long memberId) {
        MemberResponseDTO member = memberService.findMemberById(memberId);
        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    // member 수정
    @PatchMapping("/members/{member_id}")
    public ResponseEntity<?> updateMember(@PathVariable("member_id") Long memberId, @RequestBody MemberRequestDTO requestDTO){
        MemberResponseDTO updatedMember = memberService.updateMember(memberId, requestDTO);
        return new ResponseEntity<>(updatedMember, HttpStatus.OK);
    }


    // 회원삭제하는 코드구현
    @DeleteMapping("/members/{member_id}")
    public ResponseEntity<Void> deleteMember(@PathVariable("member_id") Long memberId) {
        memberService.deleteMember(memberId);
        return ResponseEntity.ok().build();
    }

    // credit조회하는 코드
    @GetMapping("/members/{member_id}/credit")
    public ResponseEntity<Long> credit(@PathVariable("member_id") Long memberId) {
        Long credit = memberService.getCreditByMemberId(memberId);
        return new ResponseEntity<>(credit, HttpStatus.OK);
    }


}
