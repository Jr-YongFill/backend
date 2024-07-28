package com.yongfill.server.domain.auth.api;

import com.yongfill.server.domain.auth.dto.AuthRequestDto;
import com.yongfill.server.domain.auth.dto.AccessTokenDto;
import com.yongfill.server.domain.auth.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthRestApi {
    private final AuthService authService;
    private final HttpServletResponse httpServletResponse;

    @Autowired
    public AuthRestApi(AuthService authService, HttpServletResponse httpServletResponse) {
        this.authService = authService;
        this.httpServletResponse = httpServletResponse;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> login(@RequestBody AuthRequestDto requestDto) {
            AccessTokenDto responseDto = authService.login(requestDto);
            System.out.println(responseDto);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    @GetMapping("/auth/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String refreshToken = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;

            // 새로운 Access 토큰 생성
            AccessTokenDto newAccessToken = this.authService.refreshToken(refreshToken);

            // 새 액세스 토큰을 헤더에 설정 
            httpServletResponse.setHeader("Access_Token", newAccessToken.getAccessToken());

            return ResponseEntity.status(HttpStatus.OK).body("newAccess Token: "+newAccessToken);

        } catch (IllegalArgumentException e) {
            // 요청이 잘못된 경우
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 토큰 요청");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }
}
