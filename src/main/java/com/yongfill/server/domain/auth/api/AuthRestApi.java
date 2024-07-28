package com.yongfill.server.domain.auth.api;

import com.yongfill.server.domain.auth.dto.AuthRequestDto;
import com.yongfill.server.domain.auth.dto.AccessTokenDto;
import com.yongfill.server.domain.auth.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthRestApi {
    private final AuthService authService;

    @PostMapping("/auth/sign-in")
    public ResponseEntity<AccessTokenDto> login(@RequestBody AuthRequestDto requestDto) {
        HttpStatus status = HttpStatus.OK;
        AccessTokenDto responseDto = authService.login(requestDto);

        return new ResponseEntity<>(responseDto, status);
    }

    @GetMapping("/auth/refresh")
    public ResponseEntity<AccessTokenDto> refreshToken(@RequestHeader("REFRESH_TOKEN") String refreshToken) {
        HttpStatus status = HttpStatus.OK;
        AccessTokenDto responseDto = authService.refreshToken(refreshToken);

        return new ResponseEntity<>(responseDto, status);
    }
}
