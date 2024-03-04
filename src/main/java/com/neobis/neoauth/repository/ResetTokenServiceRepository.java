package com.neobis.neoauth.repository;

import com.neobis.neoauth.entities.ConfirmationToken;
import com.neobis.neoauth.entities.PasswordResetToken;
import com.neobis.neoauth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResetTokenServiceRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);

    List<PasswordResetToken> findByUser(User user);
}
