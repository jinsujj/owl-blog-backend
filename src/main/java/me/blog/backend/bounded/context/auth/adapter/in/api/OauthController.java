package me.blog.backend.bounded.context.auth.adapter.in.api;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.blog.backend.bounded.context.auth.application.service.KakaoAuthService;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class OauthController {
    private final KakaoAuthService kakaoAuthService;
    private final Environment environment;

    @PostMapping("/kakao/login")
    public ResponseEntity<Void> kakaoLogin(@RequestBody kakaoRequest request, HttpServletResponse response) throws Exception {
        String jwtToken = kakaoAuthService.kakaoLogin(request.code());
        String cookie = buildCookie(jwtToken, false);
        response.addHeader("Set-Cookie", cookie);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/token/validate")
    public ResponseEntity<Map<String, Object>> checkLoginStatus(@CookieValue(value = "token", required = false) String token) {
        Map<String, Object> response = new HashMap<>();
        response.put("isValid", token != null && kakaoAuthService.validateToken(token));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/token/decode")
    public ResponseEntity<UserResponse> getCurrentUser(@CookieValue(value = "token") String token) {
        return ResponseEntity.ok(kakaoAuthService.getUserByToken(token));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        kakaoAuthService.logout(response);
        String cookie = buildCookie("", true);
        response.addHeader("Set-Cookie", cookie);
        return ResponseEntity.ok().build();
    }

    // 공통 쿠키 생성 로직
    private String buildCookie(String token, boolean expire) {
        boolean isProd = isProduction();
        StringBuilder cookieBuilder = new StringBuilder();

        cookieBuilder.append("token=").append(token)
                .append("; Path=/")
                .append("; HttpOnly");

        if (expire)
            cookieBuilder.append("; Max-Age=0");
        else
            cookieBuilder.append("; Max-Age=10800"); // 3시간


        if (isProd)
            cookieBuilder.append("; Secure; SameSite=None");
        else
            cookieBuilder.append("; SameSite=Lax");

        return cookieBuilder.toString();
    }

    private boolean isProduction() {
        String[] activeProfiles = environment.getActiveProfiles();
        for (String profile : activeProfiles) {
            if ("prod".equalsIgnoreCase(profile)) {
                return true;
            }
        }
        return false;
    }

    public record kakaoRequest(String code) {}
    public record UserResponse(Long id, String userName, String imageUrl, String email) {}
}
