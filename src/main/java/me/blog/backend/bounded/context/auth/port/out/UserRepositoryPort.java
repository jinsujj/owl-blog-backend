package me.blog.backend.bounded.context.auth.port.out;

import me.blog.backend.bounded.context.auth.domain.model.UserEntity;

import java.util.Optional;

public interface UserRepositoryPort {
    Optional<UserEntity> findByOauthId(Long oauthId);
    UserEntity save(UserEntity user);
}
