package com.yongfill.server.domain.auth.api;

import com.yongfill.server.domain.auth.dto.AccessTokenDTO;
import com.yongfill.server.domain.auth.dto.AuthRequestDTO;
import com.yongfill.server.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AuthRestAPI {
    private final AuthService authService;

    @PostMapping("/sign-in")
    public ResponseEntity<?> login(@RequestBody AuthRequestDTO requestDto) {
        AccessTokenDTO responseDto = authService.login(requestDto);
        System.out.println(responseDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    @GetMapping("/auth/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("REFRESH_TOKEN") String refreshToken) throws IllegalArgumentException, Exception {
        AccessTokenDTO newAccessToken = this.authService.refreshToken(refreshToken);

        return ResponseEntity.status(HttpStatus.OK).body(newAccessToken);
    }

//    @PostMapping("/auth/logout")
//    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authorizationHeader) {
//        String accessToken = authorizationHeader.substring(7);
//        authService.logout(accessToken);
//        return ResponseEntity.status(HttpStatus.OK).body("로그아웃 성공");
//    }
}