package me.blog.backend.bounded.context.auth.adapter.out.database;

import lombok.RequiredArgsConstructor;
import me.blog.backend.bounded.context.auth.domain.model.UserEntity;
import me.blog.backend.bounded.context.auth.port.out.UserRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {
    private final UserRepository userRepository;

    @Override
    public Optional<UserEntity> findByOauthId(Long oauthId) {
        return userRepository.findByOauthId(oauthId);
    }

    @Override
    public UserEntity save(UserEntity user) {
        return userRepository.save(user);
    }
}
