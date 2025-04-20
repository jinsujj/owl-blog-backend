package me.blog.backend.bounded.context.auth.port.in;

import jakarta.servlet.http.HttpServletResponse;
import me.blog.backend.bounded.context.auth.adapter.in.api.OauthController;

public interface KakaoUseCase {
    String kakaoLogin(String code);
    boolean validateToken(String token);
    OauthController.UserResponse getUserByToken(String token);
    void logout(HttpServletResponse response);
}
