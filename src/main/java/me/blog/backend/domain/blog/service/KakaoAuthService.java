package me.blog.backend.domain.blog.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import me.blog.backend.api.OauthController;
import me.blog.backend.common.util.JwtTokenProvider;
import me.blog.backend.domain.blog.entity.UserEntity;
import me.blog.backend.domain.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class KakaoAuthService {
    @Value("${kakao.client-id}")
    private String clientId;
    @Value("${kakao.client-secret}")
    private String clientSecret;
    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    private static final String TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private static final String USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";

    private final UserRepository userRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final JwtTokenProvider jwtTokenProvider;


    public KakaoAuthService(JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    public String kakaoLogin(String code) {
        String accessToken = getAccessToken(code);
        if (accessToken == null)
            throw new RuntimeException("Kakao access token is null");

        KakaoUserDTO userInfo = getUserInfo(accessToken);
        if(userInfo == null)
            throw new RuntimeException("Kakao userinfo is null");

        handleUserEntity(userInfo);
        return jwtTokenProvider.createToken(String.valueOf(userInfo.id), userInfo.kakaoAccount().email);
    }

    private String getAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
        try{
            ResponseEntity<String> response = restTemplate.exchange(TOKEN_URL, HttpMethod.POST, entity, String.class);

            if(response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> responseBody = mapper.readValue(response.getBody(), HashMap.class);
                return responseBody.get("access_token").toString();
            }
        }
        catch (Exception e){
            throw new RuntimeException("Failed to get access token", e);
        }
        return null;
    }

    private void handleUserEntity(KakaoUserDTO userInfo) {
        Optional<UserEntity> userEntity = userRepository.findByOauthId(userInfo.id);
        if(userEntity.isEmpty()){
            KakaoUserDTO.KakaoAccount.Profile profile = userInfo.kakaoAccount.profile;
            UserEntity user = new UserEntity(
                    userInfo.id,
                    profile.nickname,
                    profile.profileImageUrl,
                    userInfo.kakaoAccount.email,
                    userInfo.kakaoAccount.email
            );
            userRepository.save(user);
        } else {
            userEntity.get().increaseVisitCnt();
        }
    }


    public boolean validateToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }

    private KakaoUserDTO getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<KakaoUserDTO> response = restTemplate.exchange(USER_INFO_URL, HttpMethod.GET, entity, KakaoUserDTO.class);
        return response.getBody();
    }

    public OauthController.UserResponse getUserByToken(String token){
        Claims claims = jwtTokenProvider.getClaimsFromToken(token);
        String userId = claims.get("sub", String.class);
        Optional<UserEntity> user = userRepository.findByOauthId(Long.parseLong(userId));
        if(user.isEmpty())
            throw new RuntimeException("User not found");

        UserEntity currentUser =  user.get();
        return new OauthController.UserResponse(
            currentUser.getId(), currentUser.getUsername(),currentUser.getImageUrl(),currentUser.getEmail()
        );
    }


    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public record KakaoUserDTO(
            Long id,
            @JsonProperty("connected_at") Instant connectedAt,
            Properties properties,
            @JsonProperty("kakao_account") KakaoAccount kakaoAccount
    ) {
        public record Properties(
                String nickname,
                @JsonProperty("profile_image") String profileImage,
                @JsonProperty("thumbnail_image") String thumbnailImage
        ) {}

        public record KakaoAccount(
                @JsonProperty("profile_nickname_needs_agreement") Boolean profileNicknameNeedsAgreement,
                @JsonProperty("profile_image_needs_agreement") Boolean profileImageNeedsAgreement,
                Profile profile,
                @JsonProperty("has_email") Boolean hasEmail,
                @JsonProperty("email_needs_agreement") Boolean emailNeedsAgreement,
                @JsonProperty("is_email_valid") Boolean isEmailValid,
                @JsonProperty("is_email_verified") Boolean isEmailVerified,
                String email,
                @JsonProperty("has_gender") Boolean hasGender,
                @JsonProperty("gender_needs_agreement") Boolean genderNeedsAgreement,
                String gender
        ) {
            public record Profile(
                    String nickname,
                    @JsonProperty("thumbnail_image_url") String thumbnailImageUrl,
                    @JsonProperty("profile_image_url") String profileImageUrl,
                    @JsonProperty("is_default_image") Boolean isDefaultImage,
                    @JsonProperty("is_default_profile_image") Boolean isDefaultProfileImage
            ) {}
        }
    }
}
