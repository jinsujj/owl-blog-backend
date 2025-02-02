package me.blog.backend.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.blog.backend.domain.blog.service.KakaoAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class OauthController {
    private final KakaoAuthService kakaoAuthService;

    public OauthController(KakaoAuthService kakaoAuthService) {
        this.kakaoAuthService = kakaoAuthService;
    }

    @PostMapping("/kakao/login")
    public ResponseEntity<String> kakaoLogin(@RequestBody kakaoRequest request) {
        String jwtToken = kakaoAuthService.kakaoLogin(request.code);
        return ResponseEntity.ok(jwtToken);
    }

    @GetMapping("/login/status")
    public ResponseEntity<LoginStatus> checkLoginStatus(HttpServletRequest request) {
        boolean isLoggedIn = kakaoAuthService.isLoggedIn(request);
        return ResponseEntity.ok(new LoginStatus(isLoggedIn));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        kakaoAuthService.logout(response);
        return ResponseEntity.ok().build();
    }


    public record kakaoRequest(String code) {}
    public record LoginStatus(boolean isLoggedIn) {}
}
