package com.yongfill.server.domain.auth.api;

import com.yongfill.server.domain.auth.dto.AuthRequestDto;
import com.yongfill.server.domain.auth.dto.AccessTokenDto;
import com.yongfill.server.domain.auth.dto.LoginAccessTokenDto;
import com.yongfill.server.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    @GetMapping("/auth/check-email/{email}")
    public ResponseEntity<Boolean> checkEmil(@PathVariable String email) {
        HttpStatus status = HttpStatus.OK;
        Boolean responseDto = authService.checkEmil(email);

        return new ResponseEntity<>(responseDto, status);
    }

    @PostMapping("/auth/sign-in")
    public ResponseEntity<LoginAccessTokenDto> login(@RequestBody AuthRequestDto requestDto) {
        HttpStatus status = HttpStatus.OK;
        LoginAccessTokenDto responseDto = authService.login(requestDto);

        return new ResponseEntity<>(responseDto, status);
    }

    @GetMapping("/auth/refresh")
    public ResponseEntity<AccessTokenDto> refreshToken(@RequestHeader("REFRESH_TOKEN") String refreshToken) {
        HttpStatus status = HttpStatus.OK;
        AccessTokenDto responseDto = authService.refreshToken(refreshToken);

        return new ResponseEntity<>(responseDto, status);
    }

}