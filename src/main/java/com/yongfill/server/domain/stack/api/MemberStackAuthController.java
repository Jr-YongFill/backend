package com.yongfill.server.domain.stack.api;

import com.yongfill.server.domain.stack.dto.MemberStackAuthDto;
import com.yongfill.server.domain.stack.dto.QuestionStackDto;
import com.yongfill.server.domain.stack.service.MemberStackAuthService;
import com.yongfill.server.global.config.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberStackAuthController {
    private final MemberStackAuthService memberStackAuthService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/api/stacks/{stack_id}")
    public ResponseEntity<MemberStackAuthDto.AuthResponseDto> purchaseStack(@PathVariable("stack_id") Long stackId,
                                                                            @RequestHeader("Authorization") String accessToken) {
        Long memberId = jwtTokenProvider.getUserIdFromToken(accessToken.substring(7));
        HttpStatus status = HttpStatus.CREATED;
        MemberStackAuthDto.AuthResponseDto responseDto = memberStackAuthService.purchaseStack(memberId, stackId);

        return new ResponseEntity<>(responseDto, status);
    }

    @GetMapping("/api/members/{member_id}/stacks")
    public ResponseEntity<List<QuestionStackDto.StackResponseDto>> getStackInfo(@PathVariable("member_id") Long memberId) {
        HttpStatus status = HttpStatus.OK;
        List<QuestionStackDto.StackResponseDto> responseDto = memberStackAuthService.getStackInfo(memberId);

        return new ResponseEntity<>(responseDto, status);
    }
}
