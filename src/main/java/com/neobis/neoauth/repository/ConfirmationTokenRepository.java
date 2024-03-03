package com.neobis.neoauth.repository;

import com.neobis.neoauth.entities.ConfirmationToken;
import com.neobis.neoauth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {

    Optional<ConfirmationToken> findByToken(String token);

    List<ConfirmationToken> findByUser(User user);
}
