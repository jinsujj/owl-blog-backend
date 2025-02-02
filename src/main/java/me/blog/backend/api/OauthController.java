package me.blog.backend.api;

import jakarta.servlet.http.HttpServletResponse;
import me.blog.backend.domain.blog.service.KakaoAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class OauthController {
    private final KakaoAuthService kakaoAuthService;

    public OauthController(KakaoAuthService kakaoAuthService) {
        this.kakaoAuthService = kakaoAuthService;
    }

    @PostMapping("/kakao/login")
    public ResponseEntity<String> kakaoLogin(@RequestBody kakaoRequest request, HttpServletResponse response) throws Exception {
        String jwtToken = kakaoAuthService.kakaoLogin(request.code);
        response.addHeader("Set-Cookie", "token="+ jwtToken +"; Path=/; HttpOnly; Secure; SameSite=None; Max-Age=10800;");

        return ResponseEntity.ok().build();
    }

    @GetMapping("/token/validate")
    public ResponseEntity<Map<String, Object>> checkLoginStatus(@CookieValue(value = "token", required = false) String token) {
        boolean isLoggedIn = kakaoAuthService.isLoggedIn(token);

        Map<String, Object> response = new HashMap<>();
        response.put("isValid", isLoggedIn);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        kakaoAuthService.logout(response);
        response.addHeader("Set-Cookie", "token=; Path=/; HttpOnly; Secure; SameSite=None; Max-Age=0");

        return ResponseEntity.ok().build();
    }


    public record kakaoRequest(String code) {}
}
