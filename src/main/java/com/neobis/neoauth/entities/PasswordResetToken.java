package com.neobis.neoauth.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
@NoArgsConstructor
@Entity
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime resetAt;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public PasswordResetToken(String token, LocalDateTime createdAt, LocalDateTime expiresAt, LocalDateTime resetAt, User user) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.resetAt = resetAt;
        this.user = user;
    }
}
