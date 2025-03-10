package me.blog.backend.domain.blog.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(name = "owl_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "oauth_id")
    private Long oauthId;

    private String username;

    private String imageUrl;

    private String email;

    private String gender;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private int visitCnt;

    public UserEntity(Long oauthId, String username, String imageUrl, String email, String gender) {
        this.oauthId = oauthId;
        this.username = username;
        this.imageUrl = imageUrl;
        this.email = email;
        this.gender = gender;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = null;
        this.visitCnt = 0;
    }

    public void increaseVisitCnt(){
        this.visitCnt++;
    }
}
