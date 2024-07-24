package com.yongfill.server.domain.stack.api;

import com.yongfill.server.domain.stack.dto.MemberStackAuthDto;
import com.yongfill.server.domain.stack.service.MemberStackAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberStackAuthController {
    private final MemberStackAuthService memberStackAuthService;

    @PostMapping("/api/stacks/{stack_id}")
    public ResponseEntity<MemberStackAuthDto.AuthResponseDto> purchaseStack(@PathVariable("stack_id") Long stackId) {
        HttpStatus status = HttpStatus.CREATED;
        Long memberId = 1L;
        MemberStackAuthDto.AuthResponseDto responseDto = memberStackAuthService.purchaseStack(memberId, stackId);

        return new ResponseEntity<>(responseDto, status);
    }
}
