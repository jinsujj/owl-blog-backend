package me.blog.backend.bounded.context.auth.adapter.in.api;

import jakarta.servlet.http.HttpServletResponse;
import me.blog.backend.bounded.context.auth.application.service.KakaoAuthService;
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
        Map<String, Object> response = new HashMap<>();
        if (token == null) {
            response.put("isValid", false);
            return ResponseEntity.ok(response);
        }

        boolean isLoggedIn = kakaoAuthService.validateToken(token);
        response.put("isValid", isLoggedIn);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/token/decode")
    public ResponseEntity<UserResponse> getCurrentUser(@CookieValue(value ="token", required = true) String token) {
        return ResponseEntity.ok(kakaoAuthService.getUserByToken(token));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        kakaoAuthService.logout(response);
        response.addHeader("Set-Cookie", "token=; Path=/; HttpOnly; Secure; SameSite=None; Max-Age=0");

        return ResponseEntity.ok().build();
    }


    public record kakaoRequest(String code) {}

    public record UserResponse(Long id, String userName, String imageUrl, String email){}

}
