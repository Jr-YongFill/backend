package com.yongfill.server.domain.auth.api;

import com.yongfill.server.domain.auth.dto.AuthRequestDto;
import com.yongfill.server.domain.auth.dto.AuthResponseDto;
import com.yongfill.server.domain.auth.service.AuthService;
import com.yongfill.server.domain.member.dto.MemberRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthRestApi {
    private final AuthService authService;

    @Autowired
    public AuthRestApi(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> login(@RequestBody AuthRequestDto requestDto) {
        AuthResponseDto responseDto = authService.login(requestDto);
        System.out.println(responseDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

}
